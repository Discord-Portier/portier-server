package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.UserPermission
import com.github.discordportier.server.model.database.user.UserEntity
import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.model.database.user.UserRepository
import com.github.discordportier.server.service.UserAuthenticationService
import java.util.UUID
import kotlin.random.Random
import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val klogger = KotlinLogging.logger { }

@Profile("local")
@Component
class SuperUserListener {
    @EventListener
    fun listen(event: ContextRefreshedEvent) {
        val userRepository = event.applicationContext.getBean<UserRepository>()
        val userAuthenticationService = event.applicationContext.getBean<UserAuthenticationService>()

        if (userRepository.count() != 0L) {
            klogger.debug("No super user is being created")
            return
        }

        val id = UUID.randomUUID()
        val password = RandomStringUtils.randomAlphanumeric(32)
        val salt = Random.nextBytes(64)

        klogger.info { "Creating super user $id with password $password" }

        val user = UserEntity(
            id = id,
            password = userAuthenticationService.hashPassword(password, salt),
            salt = salt,
            userPermissions = mutableSetOf(),
            creator = null,
        )
        user.userPermissions.addAll(
            UserPermission.values().map {
                UserPermissionEntity(
                    id = UUID.randomUUID(),
                    permission = it,
                    user = user
                )
            }
        )
        userRepository.save(user)
    }
}
