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
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.time.Duration
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger { }
const val APP_NAME = "portier-server"

fun main(args: Array<String>): Unit = runBlocking {
    logger.info("Application starting up...")

    val parser = ArgParser(APP_NAME)
    val mongoUrl by parser.option(ArgType.String, description = "MongoDB connection URL").required()
    val mongoDatabaseName by parser.option(
        ArgType.String,
        description = "The name of the database to use"
    ).required()
    val httpPort by parser.option(ArgType.Int, description = "Port to use for HTTP server")
        .default(8080)
    parser.parse(args)
    require(httpPort in 0..Short.MAX_VALUE.toInt()) { "port $httpPort is invalid" }

    val mongo = KMongo.createClient(mongoUrl).coroutine
    val db = mongo.getDatabase(mongoDatabaseName)
    mongo.startSession() // Ensure we are authorised and it actually works out...
    onShutdown { mongo.close() }

    embeddedServer(Netty, port = httpPort) {
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

private fun onShutdown(block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(thread(start = false, block = block))
}
