package com.github.discordportier.server.model.api.response

import java.time.ZonedDateTime

data class PongResponse(
    val pingTime: ZonedDateTime,
    val pongTime: ZonedDateTime,
)
