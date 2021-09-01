package com.discordportier.server.model.event

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
sealed class WebSocketEvent

@Serializable
data class PingEvent(@Contextual val timestamp: ZonedDateTime) : WebSocketEvent()
