package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.database.user.UserPermissionEntity
import com.github.discordportier.server.service.UserAuthenticationService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class PortierAuthenticationProvider(
    private val userAuthenticationService: UserAuthenticationService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val user = userAuthenticationService.authenticate(username, password)
            ?: throw BadCredentialsException("Could not authenticate for user: $username")

        return UsernamePasswordAuthenticationToken(
            user,
            null,
            user.userPermissions.map(UserPermissionEntity::permission)
        )
    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}
