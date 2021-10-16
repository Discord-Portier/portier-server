package com.github.discordportier.server.rest

import com.github.discordportier.server.ext.awaitSingleAs
import com.github.discordportier.server.ext.io
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.api.response.UserListResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.github.discordportier.server.rest.definition.IUserResource
import com.github.discordportier.server.service.UserAuthenticationService
import java.security.Principal
import java.security.SecureRandom
import java.util.UUID
import kotlin.random.asKotlinRandom
import mu.KotlinLogging
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

private val klogger = KotlinLogging.logger { }

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
            creator = it.creator?.id,
        )
    })

    override suspend fun newUser(
        principal: Mono<Principal>,
        request: UserCreationRequest
    ): UserCreationResponse {
        val actor = principal.awaitSingleAs<AuthenticatedUser>()

        val newUserId = UUID.randomUUID()
        val salt = random.asKotlinRandom().nextBytes(64)
        val user = UserEntity(
            id = newUserId,
            password = userAuthenticationService.hashPassword(request.password, salt),
            salt = salt,
            userPermissions = mutableSetOf(),
            creator = actor.user!!,
        )
        user.userPermissions.addAll(request.permissions.map {
            UserPermissionEntity(
                id = UUID.randomUUID(),
                user = user,
                permission = it,
                creator = actor.user,
            )
        })

        // Log before save!
        klogger.info { "User ${actor.name} is creating the user $newUserId with permissions: ${request.permissions}" }

        val savedUser = io { userRepository.save(user) }

        return UserCreationResponse(savedUser.id)
    }
}
