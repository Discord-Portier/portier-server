package io.github.discordportier.server.rest.definition

import io.github.discordportier.server.annotation.ConsumeJson
import io.github.discordportier.server.annotation.ProduceJson
import io.github.discordportier.server.annotation.openapi.UnauthorisedResponse
import io.github.discordportier.server.annotation.security.Authenticated
import io.github.discordportier.server.annotation.security.PermissionRequired
import io.github.discordportier.server.model.api.request.InfractionCreationRequest
import io.github.discordportier.server.model.api.response.InfractionListResponse
import io.github.discordportier.server.model.api.response.OpenApiProblem
import io.github.discordportier.server.model.api.response.PunishmentCreationResponse
import io.github.discordportier.server.model.auth.AuthenticatedUser
import io.github.discordportier.server.model.auth.UserPermission
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

@Tag(name = "Infraction")
@RequestMapping("/v1/infraction")
@ProduceJson
@Authenticated
interface IInfractionResource {
    @GetMapping("/list")
    @Operation(summary = "Fetches a list of all infractions.")
    @ApiResponse(
        responseCode = "200",
        description = "A list of all punishments.",
        content = [Content(schema = Schema(implementation = InfractionListResponse::class))],
    )
    @UnauthorisedResponse
    @PermissionRequired(UserPermission.READ_PUNISHMENTS)
    suspend fun listInfractions(): InfractionListResponse

    @PostMapping("/new")
    @ConsumeJson
    @Operation(summary = "Creates a new infraction.")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "An infraction was created.",
            content = [Content(schema = Schema(implementation = PunishmentCreationResponse::class))],
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
    suspend fun newInfraction(
        @Parameter(hidden = true)
        authenticatedUser: AuthenticatedUser,

        @Parameter(required = true)
        @RequestBody
        request: InfractionCreationRequest,
    ): PunishmentCreationResponse
}
