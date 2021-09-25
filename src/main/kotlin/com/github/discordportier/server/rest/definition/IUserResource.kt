package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ConsumeJson
import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.model.api.request.UserCreationRequest
import com.github.discordportier.server.model.api.response.UserCreationResponse
import com.github.discordportier.server.model.api.response.UserListResponse
import com.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "User")
@RequestMapping("/v1/user")
@ProduceJson
@Authenticated
interface IUserResource {
    @GetMapping("/list")
    @Operation(summary = "Fetches a list of all users.")
    @ApiResponse(
        responseCode = "200",
        description = "A list of all users.",
        content = [Content(schema = Schema(implementation = UserListResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.READ_USER_LIST)
    fun listUsers(): UserListResponse

    @PostMapping("/new")
    @ConsumeJson
    @Operation(summary = "Creates a new user.")
    @ApiResponse(
        responseCode = "200",
        description = "A user was created.",
        content = [Content(schema = Schema(implementation = UserCreationResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.CREATE_USER)
    fun newUser(
        @Parameter(required = true)
        @RequestBody
        request: UserCreationRequest
    ): UserCreationResponse
}
