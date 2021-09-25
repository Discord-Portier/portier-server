package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema
data class PunishmentCreationResponse(
    @Schema(description = "The ID of the new punishment.", required = true)
    val id: UUID,
)
