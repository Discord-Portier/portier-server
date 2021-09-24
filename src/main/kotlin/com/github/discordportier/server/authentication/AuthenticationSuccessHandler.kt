package com.github.discordportier.server.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        // Do jack shit. We don't want to redirect anywhere.
    }
}
