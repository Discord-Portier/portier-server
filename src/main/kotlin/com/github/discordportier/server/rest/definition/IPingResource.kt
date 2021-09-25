package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.model.api.response.PongResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.ZonedDateTime

@Tag(name = "Ping")
@RequestMapping("/v1/ping")
@ProduceJson
@Authenticated
interface IPingResource {
    @GetMapping
    @Operation(summary = "Request a pong response.")
    @ApiResponse(
        responseCode = "200",
        description = "A successful pong.",
        content = [Content(schema = Schema(implementation = PongResponse::class))],
    )
    @UnauthorisedResponse
    fun ping(
        @Parameter(description = "The timestamp at which the request was sent", required = true)
        @RequestParam
        timestamp: ZonedDateTime
    ): PongResponse
}
