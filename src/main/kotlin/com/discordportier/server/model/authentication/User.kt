package com.discordportier.server.model.authentication

import io.ktor.auth.*
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
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
