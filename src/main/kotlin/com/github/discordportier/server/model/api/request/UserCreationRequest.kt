package com.github.discordportier.server.model.api.request

import com.github.discordportier.server.model.auth.UserPermission

data class UserCreationRequest(
    val password: String,
    val permissions: Set<UserPermission>,
)
