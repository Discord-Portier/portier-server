package com.github.discordportier.server.ext

import com.github.discordportier.server.exception.ServerError
import com.github.discordportier.server.exception.UnauthorisedException
import com.github.discordportier.server.model.authentication.User
import com.github.discordportier.server.model.authentication.UserPermission
import com.github.discordportier.server.model.event.ErrorEvent
import com.github.discordportier.server.model.event.WebSocketEvent
import com.github.discordportier.server.model.rest.v1.response.ErrorCode
import com.github.discordportier.server.model.rest.v1.response.ErrorPayload
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun ApplicationCall.requirePermissions(vararg permissions: UserPermission) {
    val user = this.principal<User>()
    val perms = user?.permissions
    val contains = perms?.containsAll(permissions.toSet()) == true
    if (!contains) {
        logger.debug { "${this.principal<User>()?.name} does not have: ${permissions.toSet() - this.principal<User>()?.permissions.orEmpty()}" }
        throw UnauthorisedException(*permissions)
    }
}

fun Route.portierWebSocket(
    path: String,
    protocol: String? = null,
    handler: suspend DefaultWebSocketServerSession.() -> Unit
) {
    webSocket(path, protocol) {
        try {
            handler()
        } catch (throwable: Throwable) {
            try {
                this.send(
                    jsonSerializer.encodeToString<WebSocketEvent>(
                        ErrorEvent(
                            if (throwable is ServerError) {
                                ErrorPayload(throwable.errorCode, throwable.errorDetail)
                            } else {
                                ErrorPayload(ErrorCode.INTERNAL_SERVER_ERROR, "Uncaught exception")
                            }
                        )
                    )
                )
            } catch (exception: Exception) {
                throwable.addSuppressed(exception)
            }
            throw throwable
        }
    }
}
