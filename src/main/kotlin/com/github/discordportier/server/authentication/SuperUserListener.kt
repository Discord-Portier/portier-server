package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.UserPermission
import com.github.discordportier.server.model.database.UserEntity
import com.github.discordportier.server.model.database.UserPermissionEntity
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
import org.springframework.transaction.annotation.Transactional

private val klogger = KotlinLogging.logger { }

@Profile("local")
@Component
class SuperUserListener {
    @EventListener
    @Transactional
    fun listen(event: ContextRefreshedEvent) {
        val userAuthenticationService = event.applicationContext.getBean<UserAuthenticationService>()

        if (UserEntity.count() != 0L) {
            klogger.debug("No super user is being created")
            return
        }

        val id = UUID.randomUUID()
        val password = RandomStringUtils.randomAlphanumeric(32)
        val salt = Random.nextBytes(64)

        klogger.info { "Creating super user $id with password $password" }

        val user = UserEntity.new(id) {
            this.password = userAuthenticationService.hashPassword(password, salt)
            this.salt = salt
            this.creator = null
        }
        UserPermission.values().forEach {
            UserPermissionEntity.new {
                this.permission = it
                this.user = user
                this.creator = user
            }
        }
    }
}
