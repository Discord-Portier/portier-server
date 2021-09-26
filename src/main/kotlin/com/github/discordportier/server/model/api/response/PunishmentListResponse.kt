package com.github.discordportier.server.model.api.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime
import java.util.*

@Schema
data class PunishmentListResponse(
    @ArraySchema(schema = Schema(description = "All current punishments.", required = true), minItems = 0)
    val punishments: Collection<PunishmentEntry>,
) {
    @Schema
    data class PunishmentEntry(
        @Schema(description = "The ID of the punishment.", required = true)
        val id: UUID,

        @Schema(description = "The target of the punishment.", required = true)
        val target: Long,

        @Schema(description = "The punisher of the punishment.", required = true)
        val punisher: Long,

        @Schema(description = "The creation timestamp of the punishment.", required = true)
        val creation: ZonedDateTime,

        @Schema(description = "The last modified timestamp of the punishment.", required = true)
        val lastModified: ZonedDateTime,
    )
}
