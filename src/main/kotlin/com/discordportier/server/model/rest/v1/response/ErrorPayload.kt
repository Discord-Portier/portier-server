package com.discordportier.server.model.rest.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorPayload(
    val errorCode: ErrorCode,
    val errorDetail: String,
)
