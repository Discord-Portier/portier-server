package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime
import java.util.UUID

@Schema
data class InfractionListResponse(
    @ArraySchema(schema = Schema(description = "All current infractions.", required = true), minItems = 0)
    val infractions: Collection<InfractionEntry>,
) {
    @Schema
    data class InfractionEntry(
        @Schema(description = "The ID of the infraction.", required = true)
        val id: UUID,

        @Schema(description = "The target of the infraction.", required = true)
        val target: Long,

        @Schema(description = "The punisher of the infraction.", required = true)
        val punisher: Long,

        @Schema(description = "The creation timestamp of the infraction.", required = true)
        val creation: ZonedDateTime,

        @Schema(description = "The last modified timestamp of the infraction.", required = true)
        val lastModified: ZonedDateTime,
    )
}
