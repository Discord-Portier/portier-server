package com.github.discordportier.server.model.api.response

import com.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema
data class UserListResponse(
    @ArraySchema(schema = Schema(description = "The existing users.", required = true), minItems = 0)
    val users: Collection<UserEntry>,
) {
    @Schema
    data class UserEntry(
        @Schema(description = "The ID of the user.", required = true)
        val id: UUID,

        @Schema(description = "The permissions of the user.", required = true)
        val permissions: Collection<UserPermission>,
    )
}
