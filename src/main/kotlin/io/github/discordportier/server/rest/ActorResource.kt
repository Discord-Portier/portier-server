package io.github.discordportier.server.rest

import io.github.discordportier.server.exception.PortierException
import io.github.discordportier.server.ext.io
import io.github.discordportier.server.model.api.response.ActorInfoResponse
import io.github.discordportier.server.model.api.response.ErrorCode
import io.github.discordportier.server.model.database.actor.ActorRepository
import io.github.discordportier.server.rest.definition.IActorResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ActorResource(
    private val actorRepository: ActorRepository,
) : IActorResource {
    override suspend fun fetch(id: Long): ActorInfoResponse {
        val actor = io { actorRepository.findByIdOrNull(id) }
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR, httpStatus = HttpStatus.NOT_FOUND)
        return ActorInfoResponse(
            id = actor.id,
            name = actor.username,
            discriminator = actor.discriminator,
        )
    }
}
