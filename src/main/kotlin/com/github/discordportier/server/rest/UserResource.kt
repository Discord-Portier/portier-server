package com.github.discordportier.server.rest

import com.github.discordportier.server.annotation.ConsumeJson
import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.google.common.hash.Hashing
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom
import java.util.*
import kotlin.random.asKotlinRandom

@RestController
@RequestMapping("/v1/user")
@ProduceJson
class UserResource(
    private val userRepository: UserRepository,
) {
    private val random = SecureRandom()

    @GetMapping("/list")
    fun listUsers(): List<UserEntity> = userRepository.findAll().toList()

    @PostMapping("/new")
    @ConsumeJson
    fun newUser(@RequestBody request: UserCreationRequest): UserCreationResponse {
        val existing = userRepository.getByUsernameEquals(request.username)
        if (existing != null) {
            throw PortierException(ErrorCode.USER_ALREADY_EXISTS, "Username ${request.username} already exists")
        }

        val salt = random.asKotlinRandom().nextBytes(64)
        val user = UserEntity(
            id = UUID.randomUUID(),
            username = request.username,
            password = Hashing.hmacSha512(salt).hashString(request.password, Charsets.UTF_8).asBytes(),
            salt = salt,
            userPermissions = mutableSetOf(),
        )
        user.userPermissions.addAll(request.permissions.map {
            UserPermissionEntity(
                id = UUID.randomUUID(),
                user = user,
                permission = it,
            )
        })
        userRepository.save(user)
        return UserCreationResponse(user.id, user.username)
    }
}
