package com.github.discordportier.server.model.rest.v1.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class PingPayload(
    @Contextual val timestamp: ZonedDateTime,
)
