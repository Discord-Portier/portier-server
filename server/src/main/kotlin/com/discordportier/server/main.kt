@file:JvmName("MainKt")

package com.discordportier.server

import com.discordportier.server.ext.epochMilli
import com.discordportier.server.ext.now
import com.github.discordportier.server.model.rest.response.jackson.PongJacksonPayload
import com.github.discordportier.server.rest.UrlPaths
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import mu.KotlinLogging
import java.time.Duration

private val logger = KotlinLogging.logger { }

fun main() {
    logger.info("Application starting up...")
    embeddedServer(Netty, port = 8080) {
        install(DefaultHeaders)
        install(CallLogging)
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(30L)
        }
        install(ContentNegotiation) {
            jackson()
        }

        routing {
            // Version even the websocket URL so we know exactly what the client wants.
            webSocket("/v1/ws") {
                close(reason = CloseReason(CloseReason.Codes.NORMAL, "Not implemented"))
            }

            get("/v1/${UrlPaths.PING.path}") {
                call.respond(PongJacksonPayload(now().epochMilli))
            }
        }
    }.start(wait = true)
}
