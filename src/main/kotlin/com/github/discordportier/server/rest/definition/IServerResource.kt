package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.model.api.response.ServerListResponse
import com.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Server")
@RequestMapping("/v1/server")
@ProduceJson
@Authenticated
interface IServerResource {
    @GetMapping("/list")
    @Operation(summary = "Fetches a list of all servers.")
    @ApiResponse(
        responseCode = "200",
        description = "A list of all servers.",
        content = [Content(schema = Schema(implementation = ServerListResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.READ_SERVERS)
    fun list(): ServerListResponse
}
