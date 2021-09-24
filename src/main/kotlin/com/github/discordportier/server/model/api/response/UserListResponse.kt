package com.github.discordportier.server.model.api.response

import com.github.discordportier.server.model.auth.UserPermission
import java.util.*

data class UserListResponse(
    val users: Collection<UserEntry>,
) {
    data class UserEntry(
        val id: UUID,
        val username: String,
        val permissions: Collection<UserPermission>,
    )
}
