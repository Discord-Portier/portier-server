package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.service.UserAuthenticationService
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class PortierAuthenticationProvider(
    private val userAuthenticationService: UserAuthenticationService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication !is AuthenticatedUser) {
            logger.debug { "Authentication is not AuthenticatedUser: $authentication" }
            throw BadCredentialsException("Not AuthenticatedUser")
        }

        val username = authentication.credentials.name
        val password = authentication.credentials.credentials.toString()
        logger.debug { "Authenticating $username:$password" }

        val user = userAuthenticationService.authenticate(username, password)
            ?: throw BadCredentialsException("Could not authenticate for user: $username")

        logger.debug { "Authentication successful for $username" }
        return authentication.copy(user = user)
    }

    override fun supports(authentication: Class<*>): Boolean =
        AuthenticatedUser::class.java.isAssignableFrom(authentication)
}
