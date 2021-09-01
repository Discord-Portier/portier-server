package com.discordportier.server.rest.v1

import com.discordportier.server.ext.epochMilli
import com.discordportier.server.ext.now
import com.discordportier.server.model.event.PingEvent
import com.discordportier.server.model.rest.v1.response.PingPayload
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.ping() = get("/ping") {
    PingEvent(now()).post()
    call.respond(PingPayload(now()))
}
