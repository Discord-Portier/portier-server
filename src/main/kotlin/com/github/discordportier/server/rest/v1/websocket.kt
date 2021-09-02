package com.github.discordportier.server.rest.v1

import com.github.discordportier.server.ext.jsonSerializer
import com.github.discordportier.server.ext.portierWebSocket
import com.github.discordportier.server.ext.requirePermissions
import com.github.discordportier.server.model.authentication.UserPermission
import com.github.discordportier.server.model.event.WebSocketEvent
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import java.util.*

// Does this need to be weak?
private val sockets = Collections.newSetFromMap<DefaultWebSocketServerSession>(WeakHashMap())

suspend fun WebSocketEvent.post() {
    if (sockets.isEmpty()) {
        // Nothing to send; don't serialize.
        return
    }

    val json = jsonSerializer.encodeToString(this)
    sockets.forEach {
        if (it.isActive) {
            it.send(json)
        }
    }
}

fun Route.subscriptionWebSocket(path: String = "/ws") = portierWebSocket(path) {
    this.call.requirePermissions(UserPermission.READ_PUNISHMENTS)

    try {
        sockets.add(this)
        incoming.consumeEach {}
    } finally {
        sockets.remove(this)
    }
}
