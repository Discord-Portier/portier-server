package com.github.discordportier.server.config

import com.github.discordportier.server.authentication.PortierAuthenticationFilter
import com.github.discordportier.server.authentication.PortierAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
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
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .formLogin().disable()
            .logout().disable()
            .csrf().disable()
            .headers().frameOptions().disable()

            .and()
            .addFilterBefore(
                PortierAuthenticationFilter(
                    API_MATCHER,
                    authenticationManager(),
                    authenticationEntryPoint
                ), AnonymousAuthenticationFilter::class.java
            )
            .authenticationProvider(portierAuthenticationProvider)
            .authorizeRequests().anyRequest().permitAll()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    companion object {
        private val API_MATCHER = AntPathRequestMatcher("/**")
    }
}
