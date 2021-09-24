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
        if (user?.passwordEquals(password) == true) {
            return user
        }
        return null
    }

    private fun UserEntity.passwordEquals(password: String): Boolean =
        this.password.contentEquals(
            Hashing.hmacSha512(this.salt)
                .hashString(password, Charsets.UTF_8)
                .asBytes()
        )
}
