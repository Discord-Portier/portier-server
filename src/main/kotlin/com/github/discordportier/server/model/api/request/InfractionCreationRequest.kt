package com.github.discordportier.server.model.api.request

import com.github.discordportier.server.model.punishment.InfractionCategory
import io.swagger.v3.oas.annotations.media.Schema
import java.net.URI

@Schema
data class InfractionCreationRequest(
    @Schema(description = "The target user of this infraction.", required = true)
    val target: Long,

    @Schema(description = "The punisher of this infraction.", required = true)
    val punisher: Long,

    @Schema(description = "The server this infraction belongs to.", required = true)
    val server: Long,

    @Schema(description = "The infraction category this goes under.", required = true)
    val category: InfractionCategory,

    @Schema(description = "The reason for the infraction to be placed.", required = true)
    val reason: String,

    @Schema(description = "Evidence in form of links.", required = true)
    val evidence: LinkedHashSet<URI>,
)
