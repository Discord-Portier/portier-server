package com.discordportier.server.rest.v1

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Route.subscriptionWebSocket(path: String = "/ws") = webSocket(path) {
    close(reason = CloseReason(CloseReason.Codes.NORMAL, "Not implemented"))
}
