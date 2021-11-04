package io.github.discordportier.server.model.auth

import io.github.discordportier.server.model.database.user.UserEntity
import mu.KotlinLogging
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

private val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
private val logger = KotlinLogging.logger { }

data class AuthenticatedUser(
    private val credentials: UsernamePasswordAuthenticationToken,
    val user: UserEntity?,
) : AbstractAuthenticationToken(authorities.takeIf { user != null }) {
    init {
        super.setAuthenticated(user != null)
        logger.debug { "New authenticated user: $this" }
    }

    override fun getCredentials(): UsernamePasswordAuthenticationToken = credentials
    override fun getName(): String = user?.id?.toString() ?: throw AuthenticationServiceException("Untrusted user")
    override fun getPrincipal(): UserEntity = user ?: throw AuthenticationServiceException("Untrusted user")

    override fun setAuthenticated(authenticated: Boolean) {
        require(!authenticated) { "Cannot set authenticated; use a new instance" }
        super.setAuthenticated(false)
    }
}
