package com.github.discordportier.server.service

import com.github.discordportier.server.ext.io
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.google.common.hash.Hashing
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class UserAuthenticationService(
    private val userRepository: UserRepository,
) {
    suspend fun authenticate(id: UUID, password: String): UserEntity? {
        val user = io { userRepository.getByIdEquals(id) } ?: return null
        return user.takeIf { user.password.contentEquals(hashPassword(password, user.salt)) }
    }

    fun hashPassword(password: String, salt: ByteArray): ByteArray =
        Hashing.hmacSha512(salt)
            .hashString(password, Charsets.UTF_8)
            .asBytes()
}
