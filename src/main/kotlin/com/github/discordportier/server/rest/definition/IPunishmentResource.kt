package com.github.discordportier.server.rest.definition

import com.github.discordportier.server.annotation.ConsumeJson
import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.model.api.request.PunishmentCreationRequest
import com.github.discordportier.server.model.api.response.OpenApiProblem
import com.github.discordportier.server.model.api.response.PunishmentCreationResponse
import com.github.discordportier.server.model.api.response.PunishmentListResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.auth.UserPermission
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Punishment")
@RequestMapping("/v1/punishment")
@ProduceJson
@Authenticated
interface IPunishmentResource {
    @GetMapping("/list")
    @Operation(summary = "Fetches a list of all punishments.")
    @ApiResponse(
        responseCode = "200",
        description = "A list of all punishments.",
        content = [Content(schema = Schema(implementation = PunishmentListResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.READ_PUNISHMENTS)
    fun listPunishments(): PunishmentListResponse

    @PostMapping("/new")
    @ConsumeJson
    @Operation(summary = "Creates a new punishment.")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "A punishment was created.",
            content = [Content(
                schema = Schema(implementation = PunishmentCreationResponse::class),
            )],
        ),
        ApiResponse(
            responseCode = "400",
            description = "The server is not known.",
            content = [Content(schema = Schema(implementation = OpenApiProblem::class))],
        ),
        ApiResponse(
            responseCode = "400",
            description = "The target is not known.",
            content = [Content(schema = Schema(implementation = OpenApiProblem::class))],
        ),
        ApiResponse(
            responseCode = "400",
            description = "The punisher is not known.",
            content = [Content(schema = Schema(implementation = OpenApiProblem::class))],
        ),
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.WRITE_PUNISHMENTS)
    fun newPunishment(
        @Parameter(hidden = true)
        authenticatedUser: AuthenticatedUser,

        @Parameter(required = true)
        @RequestBody
        request: PunishmentCreationRequest,
    ): PunishmentCreationResponse
}
