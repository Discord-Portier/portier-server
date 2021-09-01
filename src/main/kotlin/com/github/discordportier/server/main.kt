@file:JvmName("MainKt")

package com.discordportier.server

import com.github.discordportier.server.exception.ServerError
import com.github.discordportier.server.ext.jsonSerializer
import com.github.discordportier.server.ext.toHexString
import com.github.discordportier.server.model.authentication.User
import com.github.discordportier.server.model.rest.v1.response.ErrorCode
import com.github.discordportier.server.model.rest.v1.response.ErrorPayload
import com.github.discordportier.server.v1.ping
import com.github.discordportier.server.v1.subscriptionWebSocket
import com.google.common.hash.Hashing
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
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
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.event.Level
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

    db.getCollection<User>(User.COLLECTION)
        .ensureUniqueIndex(User::name)

    embeddedServer(Netty, port = httpPort) {
        install(DefaultHeaders)
        install(CallLogging) {
            level = Level.INFO
        }
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(30L)
        }
        install(ContentNegotiation) {
            json(jsonSerializer)
        }
        install(Authentication) {
            basic("basic") {
                realm = "Access to portier API"
                validate { credentials ->
                    val hashedPassword = Hashing.sha512()
                        .hashString(credentials.password, Charsets.UTF_8)
                        .asBytes()
                        .toHexString()
                    db.getCollection<User>(User.COLLECTION)
                        .findOne(
                            User::name eq credentials.name,
                            User::password eq hashedPassword
                        )
                }
            }
        }
        install(StatusPages) {
            exception<Throwable> {
                call.respond(
                    if (it is ServerError) {
                        ErrorPayload(it.errorCode, it.errorDetail)
                    } else {
                        ErrorPayload(ErrorCode.INTERNAL_SERVER_ERROR, "Uncaught exception")
                    }
                )
                throw it
            }
        }

        routing {
            route("/v1") {
                authenticate("basic") {
                    subscriptionWebSocket()
                    ping()
                }
            }
        }
    }.start(wait = true)
}

private fun onShutdown(block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(thread(start = false, block = block))
}
