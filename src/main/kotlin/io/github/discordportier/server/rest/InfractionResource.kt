package io.github.discordportier.server.rest

import io.github.discordportier.server.exception.PortierException
import io.github.discordportier.server.ext.io
import io.github.discordportier.server.model.api.request.InfractionCreationRequest
import io.github.discordportier.server.model.api.response.ErrorCode
import io.github.discordportier.server.model.api.response.InfractionListResponse
import io.github.discordportier.server.model.api.response.PunishmentCreationResponse
import io.github.discordportier.server.model.auth.AuthenticatedUser
import io.github.discordportier.server.model.database.actor.ActorRepository
import io.github.discordportier.server.model.database.infraction.InfractionEntity
import io.github.discordportier.server.model.database.infraction.InfractionEvidenceEntity
import io.github.discordportier.server.model.database.infraction.InfractionRepository
import io.github.discordportier.server.model.database.server.ServerRepository
import io.github.discordportier.server.rest.definition.IInfractionResource
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.RestController

@RestController
class InfractionResource(
    private val serverRepository: ServerRepository,
    private val actorRepository: ActorRepository,
    private val infractionRepository: InfractionRepository,
) : IInfractionResource {
    override suspend fun listInfractions() = InfractionListResponse(
        io { infractionRepository.findAllByHiddenIsFalse() }
            .map {
                InfractionListResponse.InfractionEntry(
                    id = it.id,
                    target = it.target.id,
                    punisher = it.punisher.id,
                    creation = it.created,
                    lastModified = it.modified,
                )
            }
    )

    override suspend fun newInfraction(
        authenticatedUser: AuthenticatedUser,
        request: InfractionCreationRequest,
    ): PunishmentCreationResponse {
        val server = io { serverRepository.findByIdOrNull(request.server) }
            ?: throw PortierException(ErrorCode.UNKNOWN_SERVER)
        val target = io { actorRepository.findByIdOrNull(request.target) }
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR)
        val punisher = io { actorRepository.findByIdOrNull(request.punisher) }
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR)

        val punishment = InfractionEntity(
            id = UUID.randomUUID(),
            server = server,
            target = target,
            punisher = punisher,
            infractionCategory = request.category,
            creator = authenticatedUser.principal,
            evidence = mutableSetOf(),
            reason = request.reason,
            lifted = false,
            hidden = false,
        )
        punishment.evidence.addAll(request.evidence.map {
            InfractionEvidenceEntity(
                id = UUID.randomUUID(),
                punishment = punishment,
                creator = authenticatedUser.principal,
                value = it.toString(),
            )
        })
        val saved = io { infractionRepository.save(punishment) }
        return PunishmentCreationResponse(saved.id)
    }
}
