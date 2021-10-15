package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ConsumeJson
import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.model.api.request.PingRequest
import com.github.discordportier.server.model.api.response.PongResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Ping")
@RequestMapping("/v1/ping")
@ProduceJson
@Authenticated
interface IPingResource {
    @PostMapping
    @Operation(summary = "Request a pong response.")
    @ApiResponse(
        responseCode = "200",
        description = "A successful pong.",
        content = [Content(schema = Schema(implementation = PongResponse::class))],
    )
    @ConsumeJson
    @UnauthorisedResponse
    suspend fun ping(
        @Parameter(required = true)
        @RequestBody
        request: PingRequest
    ): PongResponse
}
