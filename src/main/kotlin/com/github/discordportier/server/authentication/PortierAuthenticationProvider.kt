package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.service.UserAuthenticationService
import java.util.UUID
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

private val klogger = KotlinLogging.logger { }

@Component
class PortierAuthenticationProvider(
    private val userAuthenticationService: UserAuthenticationService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication !is AuthenticatedUser) {
            klogger.debug { "Authentication is not AuthenticatedUser: $authentication" }
            throw BadCredentialsException("Not AuthenticatedUser")
        }

        val userId = try {
            UUID.fromString(authentication.credentials.name)
        } catch (ex: IllegalArgumentException) {
            throw BadCredentialsException("Invalid user id", ex)
        }
        val password = authentication.credentials.credentials.toString()
        klogger.debug { "Authenticating $userId:$password" }

        val user = userAuthenticationService.authenticate(userId, password)
            ?: throw BadCredentialsException("Could not authenticate for user: $userId")

        klogger.debug { "Authentication successful for $userId" }
        return authentication.copy(user = user)
    }

    override fun supports(authentication: Class<*>): Boolean =
        AuthenticatedUser::class.java.isAssignableFrom(authentication)
}
