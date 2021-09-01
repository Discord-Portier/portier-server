package com.github.discordportier.server.model.event

import com.github.discordportier.server.model.rest.v1.response.ErrorPayload
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
sealed class WebSocketEvent

@Serializable
@SerialName("ErrorEvent")
data class ErrorEvent(@Contextual val payload: ErrorPayload) : WebSocketEvent()

@Serializable
@SerialName("PingEvent")
data class PingEvent(@Contextual val timestamp: ZonedDateTime) : WebSocketEvent()
