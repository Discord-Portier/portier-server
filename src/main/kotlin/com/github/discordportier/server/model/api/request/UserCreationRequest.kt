package com.github.discordportier.server.model.api.request

import com.github.discordportier.server.model.auth.UserPermission

data class UserCreationRequest(
    val username: String,
    val password: String,
    val permissions: Set<UserPermission>,
)
