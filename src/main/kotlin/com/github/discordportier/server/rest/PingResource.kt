package com.github.discordportier.server.rest

import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.api.response.PongResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@Tag(name = "Ping")
@RestController
@RequestMapping("/v1/ping")
@Authenticated
class PingResource {
    @GetMapping
    fun ping(@RequestParam timestamp: ZonedDateTime) =
        PongResponse(
            pingTime = timestamp,
            pongTime = now(),
        )
}
