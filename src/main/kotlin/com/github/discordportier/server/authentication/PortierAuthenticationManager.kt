package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.service.UserAuthenticationService
import java.util.UUID
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

private val klogger = KotlinLogging.logger {}

@Component
class PortierAuthenticationManager(
    private val userAuthenticationService: UserAuthenticationService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {
        klogger.debug { "Authenticating ${authentication.name} with password ${authentication.credentials}" }
        val userId = try {
            UUID.fromString(authentication.name)
        } catch (ex: IllegalArgumentException) {
            throw BadCredentialsException("Invalid id: ${authentication.name}", ex)
        }
        val password = authentication.credentials.toString()
        klogger.trace("Correctly parsed credentials")

        val user = userAuthenticationService.authenticate(userId, password)
            ?: throw BadCredentialsException("Invalid credentials provided for $userId")
        klogger.debug { "Found user for $userId: $user" }

        AuthenticatedUser(
            UsernamePasswordAuthenticationToken(authentication.name, password),
            user,
        )
    }
}
