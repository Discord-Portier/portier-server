package com.github.discordportier.server.exception

import com.github.discordportier.server.model.api.response.ErrorCode
import org.springframework.http.HttpStatus
import org.zalando.problem.StatusType

open class PortierException(
    val errorCode: ErrorCode,

    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    val status = errorCode.http.zalandoStatus

    override fun getLocalizedMessage(): String = "[$errorCode] $message"
}
