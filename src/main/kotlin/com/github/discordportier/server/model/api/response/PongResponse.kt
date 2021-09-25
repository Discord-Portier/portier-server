package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

@Schema
data class PongResponse(
    @Schema(description = "The time at which the request was sent from the client.", required = true)
    val pingTime: ZonedDateTime,

    @Schema(description = "The time at which the request was received.", required = true)
    val pongTime: ZonedDateTime,
)
