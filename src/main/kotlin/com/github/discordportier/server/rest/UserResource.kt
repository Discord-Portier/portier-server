package com.github.discordportier.server.rest

import com.github.discordportier.server.annotation.ConsumeJson
import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.api.response.UserListResponse
import com.github.discordportier.server.model.auth.UserPermission
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.github.discordportier.server.service.UserAuthenticationService
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom
import java.util.*
import kotlin.random.asKotlinRandom

@RestController
@RequestMapping("/v1/user")
@ProduceJson
@Authenticated
class UserResource(
    private val userRepository: UserRepository,
    private val userAuthenticationService: UserAuthenticationService,
) {
    private val random = SecureRandom()

    @GetMapping("/list")
    @PermissionRequired(UserPermission.READ_USER_LIST)
    fun listUsers(): UserListResponse = UserListResponse(userRepository.findAll().map {
        UserListResponse.UserEntry(
            id = it.id,
            permissions = it.userPermissions.map(UserPermissionEntity::permission),
        )
    })

    @PostMapping("/new")
    @ConsumeJson
    @PermissionRequired(UserPermission.CREATE_USER)
    fun newUser(@RequestBody request: UserCreationRequest): UserCreationResponse {
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
        userRepository.save(user)
        return UserCreationResponse(user.id)
    }
}
