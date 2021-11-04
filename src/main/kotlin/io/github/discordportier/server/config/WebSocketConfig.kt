package io.github.discordportier.server.config

import io.github.discordportier.server.service.PortierWebSocketHandlerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@Configuration
class WebSocketConfig(private val webSocketHandlerV1: PortierWebSocketHandlerService) {
    @Bean
    fun handlerMapping(): HandlerMapping = SimpleUrlHandlerMapping(
        mapOf("/v1/ws" to webSocketHandlerV1),
        -1 // Run before annotated controllers
    )
}
