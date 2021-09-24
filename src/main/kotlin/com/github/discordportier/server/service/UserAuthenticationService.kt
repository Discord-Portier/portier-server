package com.github.discordportier.server.service

import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.google.common.hash.Hashing
import org.springframework.stereotype.Service

@Service
class UserAuthenticationService(
    private val userRepository: UserRepository,
) {
    fun authenticate(username: String, password: String): UserEntity? {
        val user = userRepository.getByUsernameEquals(username)
        if (user?.password?.contentEquals(hashPassword(password, user.salt)) == true) {
            return user
        }
        return null
    }

    fun hashPassword(password: String, salt: ByteArray): ByteArray =
        Hashing.hmacSha512(salt)
            .hashString(password, Charsets.UTF_8)
            .asBytes()
}
