package com.github.discordportier.server.exception

import com.github.discordportier.server.model.api.response.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.socket.CloseStatus

open class PortierException(
    val errorCode: ErrorCode,
    val httpStatus: HttpStatus = errorCode.http,
    val wsStatus: CloseStatus = errorCode.webSocket,

    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    val status = httpStatus.zalandoStatus

    override fun getLocalizedMessage(): String = "[$errorCode] $message"
}
