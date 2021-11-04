package io.github.discordportier.server.model.api.request

import io.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class UserCreationRequest(
    @Schema(description = "The password to use for identifying the new user.", required = true)
    val password: String,

    @Schema(description = "The permissions the new user owns.", required = true)
    val permissions: Set<UserPermission>,
)
