package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ActorInfoResponse(
    @Schema(description = "The ID of the actor.", required = true)
    val id: Long,

    @Schema(description = "The name of the actor.", required = true)
    val name: String,

    @Schema(description = "The discriminator of the actor.", required = true, minimum = "0", maximum = "9999")
    val discriminator: Int,
)
