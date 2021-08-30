package com.discordportier.server.rest.v1

import com.discordportier.server.ext.epochMilli
import com.discordportier.server.ext.now
import com.discordportier.server.model.rest.v1.response.PingPayload
import com.discordportier.server.model.rest.v1.response.PossiblyErrorResponse
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.ping() = get("/ping") {
    call.respond(PingPayload(now()))
}
