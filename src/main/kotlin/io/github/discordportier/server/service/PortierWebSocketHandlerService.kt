package io.github.discordportier.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.discordportier.server.exception.PortierException
import io.github.discordportier.server.ext.io
import io.github.discordportier.server.model.api.response.ErrorCode
import io.github.discordportier.server.model.api.websocket.ErrorMessage
import io.github.discordportier.server.model.api.websocket.IMessage
import io.github.discordportier.server.model.auth.AuthenticatedUser
import java.util.Collections
import java.util.WeakHashMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

private val klogger = KotlinLogging.logger { }

@Service
class PortierWebSocketHandlerService(
    private val objectMapper: ObjectMapper,
) : WebSocketHandler {
    private val sessions: MutableMap<String, WebSocketSession> = Collections.synchronizedMap(WeakHashMap())

    suspend fun publish(message: IMessage) {
        val json = io { objectMapper.writeValueAsBytes(message) }
        var suppressedExceptions: MutableList<Exception>? = null
        for (it in sessions.values) {
            if (!it.isOpen) {
                continue
            }

            try {
                it.send(publish {
                    WebSocketMessage(WebSocketMessage.Type.TEXT, io { it.bufferFactory().wrap(json) })
                }).awaitSingleOrNull()
            } catch (ignored: CancellationException) {
                // Ignore this: the job is cancelled and is a NORMAL exit of the coroutine.
            } catch (ex: Exception) {
                if (suppressedExceptions == null) {
                    suppressedExceptions = mutableListOf(ex)
                } else {
                    suppressedExceptions.add(ex)
                }
            }
        }
        if (suppressedExceptions?.isNotEmpty() == true) {
            val ex = PortierException(ErrorCode.UNKNOWN)
            suppressedExceptions.forEach(ex::addSuppressed)
            throw ex
        }
    }

    override fun handle(session: WebSocketSession): Mono<Void?> = mono {
        try {
            handle0(session)
        } catch (ignored: CancellationException) {
            // Ignore this: the job is cancelled and is a NORMAL exit of the coroutine.
        } catch (ex: PortierException) {
            session.err(ex.errorCode, ex.wsStatus)
            klogger.warn(ex) { "Exception in WS session of ${session.id} / ${session.handshakeInfo.remoteAddress}" }
        } catch (ex: Exception) {
            session.err(ErrorCode.UNKNOWN)
            klogger.error(ex) { "Exception in WS session of ${session.id} / ${session.handshakeInfo.remoteAddress}" }
        }
        sessions.remove(session.id, session)
        null
    }

    private suspend fun handle0(session: WebSocketSession) {
        if (sessions.getOrPut(session.id) { session } !== session) {
            session.err(ErrorCode.WEB_SOCKET_ID_HIJACK_DISALLOWED)
            return
        }

        // We have now set up the socket for success.
        // As the messages will be published elsewhere, we have nothing to worry about here now.

        try {
            // Block the coroutine on receiving messages OR ending connection.
            if (session.receive().awaitFirst() != null) {
                val user = session.handshakeInfo.principal.awaitSingleOrNull() as? AuthenticatedUser
                val id = when {
                    user != null -> "user id ${user.name} (session ${session.id})"
                    else -> "session ${session.id}"
                }
                klogger.warn { "Received message from $id / ${session.handshakeInfo.remoteAddress}: dropping connection" }
                session.err(ErrorCode.WEB_SOCKET_CANNOT_RECEIVE)
            }
        } catch (ignored: NoSuchElementException) {
            // No message was received. All is well :)
        }
    }

    private suspend fun WebSocketSession.sendJsonMessage(value: IMessage?) {
        send(publish {
            val payload = io { bufferFactory().wrap(objectMapper.writeValueAsBytes(value)) }
            send(WebSocketMessage(WebSocketMessage.Type.TEXT, payload))
        }).awaitSingleOrNull()
    }

    private suspend fun WebSocketSession.closeWith(status: CloseStatus, message: IMessage?) {
        sendJsonMessage(message)
        close(status).awaitSingleOrNull()
    }

    private suspend fun WebSocketSession.err(errorCode: ErrorCode, closeStatus: CloseStatus = errorCode.webSocket) =
        closeWith(closeStatus.withReason(errorCode.name), ErrorMessage(errorCode))
}
