package com.github.discordportier.server.rest

import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.ext.io
import com.github.discordportier.server.model.api.request.PunishmentCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.PunishmentCreationResponse
import com.github.discordportier.server.model.api.response.PunishmentListResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.database.actor.ActorRepository
import com.github.discordportier.server.model.database.punishment.PunishmentEntity
import com.github.discordportier.server.model.database.punishment.PunishmentEvidenceEntity
import com.github.discordportier.server.model.database.punishment.PunishmentRepository
import com.github.discordportier.server.model.database.server.ServerRepository
import com.github.discordportier.server.rest.definition.IPunishmentResource
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.RestController

@RestController
class PunishmentResource(
    private val serverRepository: ServerRepository,
    private val actorRepository: ActorRepository,
    private val punishmentRepository: PunishmentRepository,
) : IPunishmentResource {
    override suspend fun listPunishments() = PunishmentListResponse(
        io { punishmentRepository.findAllByHiddenIsFalse() }
            .map {
                PunishmentListResponse.PunishmentEntry(
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

        val punishment = PunishmentEntity(
            id = UUID.randomUUID(),
            server = server,
            target = target,
            punisher = punisher,
            punishmentCategory = request.category,
            creator = authenticatedUser.principal,
            evidence = mutableSetOf(),
            reason = request.reason,
            lifted = false,
            hidden = false,
        )
        punishment.evidence.addAll(request.evidence.map {
            PunishmentEvidenceEntity(
                id = UUID.randomUUID(),
                punishment = punishment,
                creator = authenticatedUser.principal,
                value = it.toString(),
            )
        })
        val saved = io { punishmentRepository.save(punishment) }
        return PunishmentCreationResponse(saved.id)
    }
}
