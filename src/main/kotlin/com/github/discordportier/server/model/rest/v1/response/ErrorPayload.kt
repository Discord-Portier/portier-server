package com.github.discordportier.server.model.rest.v1.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorPayload(
    @SerialName("error_code") val errorCode: ErrorCode,
    @SerialName("error_detail") val errorDetail: String,
)
