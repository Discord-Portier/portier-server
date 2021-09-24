package com.github.discordportier.server.config

import com.github.discordportier.server.authentication.PortierAuthenticationProvider
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val portierAuthenticationProvider: PortierAuthenticationProvider,
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(portierAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity) {
//        http.authorizeRequests()
//            .anyRequest()
//            .anonymous()
//            .and()
//            .csrf().disable()
//            .headers().frameOptions().disable()
        http
            .csrf().disable()
            .headers().frameOptions().disable()

            .and()
            .authenticationProvider(portierAuthenticationProvider)
            .authorizeRequests()
            .anyRequest().authenticated()
            .and().httpBasic().realmName(REALM)
    }

    companion object {
        const val REALM = "portier"
    }
}
