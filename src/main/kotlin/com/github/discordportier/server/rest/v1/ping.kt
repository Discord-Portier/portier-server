package com.github.discordportier.server.rest.v1

import com.github.discordportier.server.model.event.PingEvent
import com.github.discordportier.server.model.rest.v1.response.PingPayload
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime.now

fun Route.v1Ping() = get("/ping") {
    val time = now()
    PingEvent(time).post()
    this.call.respond<PingResponse>(PingResponse.Ok(PingPayload(time)))
}

@Serializable
private sealed class PingResponse {
    @Serializable
    @SerialName("ping")
    data class Ok(val payload: PingPayload) : PingResponse()
}
