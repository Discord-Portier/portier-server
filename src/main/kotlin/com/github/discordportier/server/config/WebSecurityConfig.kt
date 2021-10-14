package com.github.discordportier.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.discordportier.server.authentication.PortierAuthenticationManager
import com.github.discordportier.server.exception.zalandoStatus
import com.github.discordportier.server.model.api.response.ErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.zalando.problem.Problem

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    private val portierAuthenticationManager: PortierAuthenticationManager,
) {
    private val problemMediaType = MediaType.parseMediaType("application/problem+json")

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, objectMapper: ObjectMapper): SecurityWebFilterChain =
        http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .csrf { it.disable() }
            .headers { it.frameOptions().disable() }

            .authorizeExchange { it.anyExchange().authenticated() }
            .httpBasic { it.disable() }
            .addFilterAfter(
                AuthenticationWebFilter(portierAuthenticationManager).also {
                    it.setAuthenticationFailureHandler { webFilterExchange, exception ->
                        mono {
                            val body = Problem.builder()
                                .withStatus(HttpStatus.UNAUTHORIZED.zalandoStatus)
                                .withTitle("Unauthorized")
                                .withDetail("Unauthorized")
                                .with("error_code", ErrorCode.UNAUTHORIZED)
                                .build()
                            val serialisedBody = withContext(Dispatchers.IO) { objectMapper.writeValueAsString(body) }

                            ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .contentType(problemMediaType)
                                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic")
                                .bodyValue(serialisedBody)
                                .awaitSingle()
                                .writeTo(webFilterExchange.exchange, DefaultServerResponseContext)
                                .awaitSingleOrNull()
                        }
                    }
                },
                SecurityWebFiltersOrder.REACTOR_CONTEXT
            )

            .build()
}

private object DefaultServerResponseContext : ServerResponse.Context {
    private val handlerStrategies = HandlerStrategies.withDefaults()

    override fun messageWriters(): List<HttpMessageWriter<*>> =
        handlerStrategies.messageWriters()

    override fun viewResolvers(): List<ViewResolver> =
        handlerStrategies.viewResolvers()
}
