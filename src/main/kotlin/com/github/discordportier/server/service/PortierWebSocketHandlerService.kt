package com.github.discordportier.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.websocket.ErrorMessage
import com.github.discordportier.server.model.api.websocket.IMessage
import java.util.Collections
import java.util.WeakHashMap
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

private val logger = KotlinLogging.logger { }

@Service
class PortierWebSocketHandlerService(private val objectMapper: ObjectMapper) : WebSocketHandler {
    private val sessions: MutableMap<String, WebSocketSession> = Collections.synchronizedMap(WeakHashMap())

    fun publish(message: IMessage) {
        val json = TextMessage(objectMapper.writeValueAsString(message))
        var suppressedExceptions: MutableList<Exception>? = null
        for (it in sessions.values) {
            try {
                it.sendMessage(json)
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

    override fun supportsPartialMessages(): Boolean = true

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.compute(session.id) { _, existing ->
            if (existing != null) {
                session.err(ErrorCode.WEB_SOCKET_ID_HIJACK_DISALLOWED)
                existing
            } else {
                session
            }
        }
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        logger.warn { "Received message from ${session.id} / ${session.remoteAddress}; dropping connection" }
        session.err(ErrorCode.WEB_SOCKET_CANNOT_RECEIVE)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.warn(exception) { "Transport error to ${session.id} / ${session.remoteAddress}" }
        if (exception is PortierException) {
            session.err(exception.errorCode, exception.wsStatus)
        } else {
            session.err(ErrorCode.WEB_SOCKET_TRANSPORT_ERROR)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        if (closeStatus.code != CloseStatus.NORMAL.code) {
            logger.warn { "Session ${session.id} / ${session.remoteAddress} closed with status $closeStatus" }
        }

        sessions.remove(session.id)
    }

    private fun WebSocketSession.sendJsonMessage(value: IMessage?) {
        sendMessage(TextMessage(objectMapper.writeValueAsString(value)))
    }

    private fun WebSocketSession.closeWith(status: CloseStatus, message: IMessage?) {
        sendJsonMessage(message)
        close(status)
    }

    private fun WebSocketSession.err(errorCode: ErrorCode, closeStatus: CloseStatus = errorCode.webSocket) =
        closeWith(errorCode.webSocket, ErrorMessage(errorCode))
}
