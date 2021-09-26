package com.github.discordportier.server.rest

import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.response.ActorInfoResponse
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.database.actor.ActorRepository
import com.github.discordportier.server.rest.definition.IActorResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.RestController

@RestController
class ActorResource(
    private val actorRepository: ActorRepository,
) : IActorResource {
    override fun fetch(id: Long): ActorInfoResponse {
        val actor = actorRepository.findByIdOrNull(id)
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR)
        return ActorInfoResponse(
            id = actor.id,
            name = actor.username,
            discriminator = actor.discriminator,
        )
    }
}
