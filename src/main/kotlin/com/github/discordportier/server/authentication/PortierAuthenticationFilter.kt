package com.github.discordportier.server.authentication

import com.github.discordportier.server.model.auth.AuthenticatedUser
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val klogger = KotlinLogging.logger { }
private const val ATTRIBUTE_CREDENTIALS = "PortierAuthFilterCredentials"

class PortierAuthenticationFilter(
    authenticatedMatcher: RequestMatcher,
    authenticationManager: AuthenticationManager,
    authenticationEntryPoint: AuthenticationEntryPoint,
) : AbstractAuthenticationProcessingFilter(authenticatedMatcher) {
    init {
        this.authenticationManager = authenticationManager
        this.setAuthenticationFailureHandler(AuthenticationEntryPointFailureHandler(authenticationEntryPoint))
    }

    private val authenticationConverter = BasicAuthenticationConverter()

    override fun requiresAuthentication(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        if (!super.requiresAuthentication(request, response)) {
            return false
        }

        val credentials = authenticationConverter.convert(request)
        if (credentials != null) {
            request.setAttribute(ATTRIBUTE_CREDENTIALS, credentials)
            return true
        }
        return false
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        klogger.debug { "Authenticating for ${request.remoteAddr}" }
        val credentials = request.getAttribute(ATTRIBUTE_CREDENTIALS) as UsernamePasswordAuthenticationToken
        return AuthenticatedUser(
            credentials,
            null
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.getContext().authentication = authResult
        klogger.debug { "Set SecurityContextHolder to $authResult" }
        chain.doFilter(request, response)
    }
}
