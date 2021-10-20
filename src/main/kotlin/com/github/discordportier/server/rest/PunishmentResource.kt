package com.github.discordportier.server.rest

import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.ext.io
import com.github.discordportier.server.model.api.request.PunishmentCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.InfractionListResponse
import com.github.discordportier.server.model.api.response.PunishmentCreationResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.database.actor.ActorRepository
import com.github.discordportier.server.model.database.infraction.InfractionEntity
import com.github.discordportier.server.model.database.infraction.InfractionEvidenceEntity
import com.github.discordportier.server.model.database.infraction.InfractionRepository
import com.github.discordportier.server.model.database.server.ServerRepository
import com.github.discordportier.server.rest.definition.IPunishmentResource
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.RestController

@RestController
class PunishmentResource(
    private val serverRepository: ServerRepository,
    private val actorRepository: ActorRepository,
    private val infractionRepository: InfractionRepository,
) : IPunishmentResource {
    override suspend fun listPunishments() = InfractionListResponse(
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

    override suspend fun newPunishment(
        authenticatedUser: AuthenticatedUser,
        request: PunishmentCreationRequest,
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
