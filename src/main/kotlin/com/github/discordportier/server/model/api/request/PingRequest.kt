package com.github.discordportier.server.model.api.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

@Schema
data class PingRequest(
    @Schema(description = "The timestamp at which the request was sent.", required = true)
    val timestamp: ZonedDateTime,
)
