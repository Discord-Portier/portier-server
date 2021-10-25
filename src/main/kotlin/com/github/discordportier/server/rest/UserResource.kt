package com.github.discordportier.server.rest

import com.github.discordportier.server.ext.awaitSingleAs
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.api.response.UserListResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.database.UserEntity
import com.github.discordportier.server.model.database.UserPermissionEntity
import com.github.discordportier.server.rest.definition.IUserResource
import com.github.discordportier.server.service.UserAuthenticationService
import java.security.Principal
import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import mu.KotlinLogging
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

private val klogger = KotlinLogging.logger { }

@RestController
class UserResource(
    private val userAuthenticationService: UserAuthenticationService,
) : IUserResource {
    private val random = SecureRandom()

    override suspend fun listUsers() =
        newSuspendedTransaction {
            UserListResponse(UserEntity.all().map {
                UserListResponse.UserEntry(
                    id = it.id.value,
                    permissions = it.permissions.map(UserPermissionEntity::permission),
                    creator = it.creator?.id?.value,
                )
            })
        }

    override suspend fun newUser(
        principal: Mono<Principal>,
        request: UserCreationRequest
    ): UserCreationResponse {
        val actor = principal.awaitSingleAs<AuthenticatedUser>()

        val salt = random.asKotlinRandom().nextBytes(64)

        val userId = newSuspendedTransaction {
            val user = UserEntity.new {
                this.password = userAuthenticationService.hashPassword(request.password, salt)
                this.salt = salt
                //this.creator = actor.user!!
            }
            request.permissions.forEach {
                UserPermissionEntity.new {
                    this.user = user
                    this.permission = it
                    //this.creator = actor.user
                }
            }

            user.id.value
        }

        klogger.info { "User ${actor.name} is creating the user $userId with permissions: ${request.permissions}" }

        return UserCreationResponse(userId)
    }
}
