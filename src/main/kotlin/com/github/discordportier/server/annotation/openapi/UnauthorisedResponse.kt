package com.github.discordportier.server.annotation.openapi

import com.github.discordportier.server.model.api.response.OpenApiProblem
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@SecurityRequirement(name = "basicAuth")
@ApiResponse(
    responseCode = "401",
    description = "You were disallowed from using this endpoint." +
            " You might be missing permissions, or you're simply unauthenticated.",
    content = [Content(schema = Schema(implementation = OpenApiProblem::class))],
)
annotation class UnauthorisedResponse
