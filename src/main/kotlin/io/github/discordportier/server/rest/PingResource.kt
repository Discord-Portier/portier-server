package io.github.discordportier.server.rest

import io.github.discordportier.server.ext.now
import io.github.discordportier.server.model.api.request.PingRequest
import io.github.discordportier.server.model.api.response.PongResponse
import io.github.discordportier.server.rest.definition.IPingResource
import org.springframework.web.bind.annotation.RestController

@RestController
class PingResource : IPingResource {
    override suspend fun ping(request: PingRequest) =
        PongResponse(
            pingTime = request.timestamp,
            pongTime = now(),
        )
}
