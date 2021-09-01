package com.github.discordportier.server.model.authentication

import io.ktor.auth.*
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class User(
    val name: String,
    /**
     * SHA-512 hashed password.
     */
    val password: String,
    val permissions: Set<UserPermission>,

    val _id: Id<User> = newId(),
) : Principal {
    companion object {
        const val COLLECTION = "users"
    }
}
