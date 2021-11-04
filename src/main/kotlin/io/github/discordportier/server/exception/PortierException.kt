package io.github.discordportier.server.exception

import io.github.discordportier.server.model.api.response.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.socket.CloseStatus

open class PortierException(
    val errorCode: ErrorCode,
    val httpStatus: HttpStatus = errorCode.http,
    val wsStatus: CloseStatus = errorCode.webSocket,

    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    override fun getLocalizedMessage(): String = "[$errorCode] $message"
}
