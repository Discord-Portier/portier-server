package com.github.discordportier.server.rest

import com.github.discordportier.server.ext.io
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.api.response.UserListResponse
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.github.discordportier.server.rest.definition.IUserResource
import com.github.discordportier.server.service.UserAuthenticationService
import java.security.SecureRandom
import java.util.UUID
import kotlin.random.asKotlinRandom
import org.springframework.web.bind.annotation.RestController

@RestController
class UserResource(
    private val userRepository: UserRepository,
    private val userAuthenticationService: UserAuthenticationService,
) : IUserResource {
    private val random = SecureRandom()

    override suspend fun listUsers() = UserListResponse(io { userRepository.findAll() }.map {
        UserListResponse.UserEntry(
            id = it.id,
            permissions = it.userPermissions.map(UserPermissionEntity::permission),
        )
    })

    override suspend fun newUser(request: UserCreationRequest): UserCreationResponse {
        val salt = random.asKotlinRandom().nextBytes(64)
        val user = UserEntity(
            id = UUID.randomUUID(),
            password = userAuthenticationService.hashPassword(request.password, salt),
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
        val savedUser = io { userRepository.save(user) }
        return UserCreationResponse(savedUser.id)
    }
}
