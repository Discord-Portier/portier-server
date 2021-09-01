package com.github.discordportier.server.v1

import com.github.discordportier.server.ext.epochMilli
import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.event.PingEvent
import com.github.discordportier.server.model.rest.v1.response.PingPayload
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.ping() = get("/ping") {
    PingEvent(now()).post()
    call.respond(PingPayload(now()))
}
