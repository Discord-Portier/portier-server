package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.model.api.response.ActorInfoResponse
import com.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Ping")
@RequestMapping("/v1/actor")
@ProduceJson
@Authenticated
interface IActorResource {
    @GetMapping("/{id}")
    @Operation(summary = "Fetch information about an actor.")
    @ApiResponse(
        responseCode = "200",
        description = "An actor's information.",
        content = [Content(schema = Schema(implementation = ActorInfoResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.READ_ACTORS)
    fun fetch(@PathVariable id: Long): ActorInfoResponse
}
