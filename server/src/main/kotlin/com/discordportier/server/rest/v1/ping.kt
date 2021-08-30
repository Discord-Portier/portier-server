package com.discordportier.server.rest.v1

import com.discordportier.server.ext.epochMilli
import com.discordportier.server.ext.now
import com.github.discordportier.server.model.rest.response.jackson.PongJacksonPayload
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.ping() = get("/ping") {
    call.respond(PongJacksonPayload(now().epochMilli))
}
