package com.github.discordportier.server.model.api.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class PunishmentCreationRequest(
    @Schema(description = "The target user of this punishment.", required = true)
    val target: Long,

    @Schema(description = "The punisher of this punishment.", required = true)
    val punisher: Long,

    @Schema(description = "The server this punishment belongs to.", required = true)
    val server: Long,
)
