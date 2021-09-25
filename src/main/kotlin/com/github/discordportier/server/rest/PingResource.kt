package com.github.discordportier.server.rest

import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.api.response.PongResponse
import com.github.discordportier.server.rest.definition.IPingResource
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
class PingResource : IPingResource {
    override fun ping(timestamp: ZonedDateTime) =
        PongResponse(
            pingTime = timestamp,
            pongTime = now(),
        )
}
