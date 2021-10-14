package com.github.discordportier.server.rest

import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.api.request.PingRequest
import com.github.discordportier.server.model.api.response.PongResponse
import com.github.discordportier.server.rest.definition.IPingResource
import org.springframework.web.bind.annotation.RestController

@RestController
class PingResource : IPingResource {
    override suspend fun ping(request: PingRequest) =
        PongResponse(
            pingTime = request.timestamp,
            pongTime = now(),
        )
}
