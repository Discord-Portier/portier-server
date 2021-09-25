package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class ServerListResponse(
    @ArraySchema(schema = Schema(description = "The servers currently registered.", required = true), minItems = 0)
    val servers: Collection<ServerEntry>,
) {
    @Schema
    data class ServerEntry(
        @Schema(description = "The ID of the server.", required = true)
        val id: Long,

        @Schema(description = "The name of the server.", required = true)
        val name: String,
    )
}
