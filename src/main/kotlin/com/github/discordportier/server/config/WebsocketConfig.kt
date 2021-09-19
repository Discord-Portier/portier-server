package com.github.discordportier.server.config

import com.github.discordportier.server.service.PortierWebSocketHandlerService
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebsocketConfig(private val webSocketHandlerV1: PortierWebSocketHandlerService) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandlerV1, "/v1/ws")
    }
}
