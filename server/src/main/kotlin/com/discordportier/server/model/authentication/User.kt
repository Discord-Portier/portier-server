package com.discordportier.server.model.authentication

import io.ktor.auth.*
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class User(
    val name: String,
    val password: String,
    val permissions: Set<UserPermission>,

    val _id: Id<User> = newId(),
) : Principal {
    companion object {
        const val COLLECTION = "users"
    }
}
