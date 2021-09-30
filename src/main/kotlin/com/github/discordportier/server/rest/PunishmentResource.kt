package com.github.discordportier.server.rest

import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.request.PunishmentCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.PunishmentCreationResponse
import com.github.discordportier.server.model.api.response.PunishmentListResponse
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.database.actor.ActorRepository
import com.github.discordportier.server.model.database.punishment.PunishmentEntity
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
    override fun listPunishments() = PunishmentListResponse(
        punishmentRepository.findAllByHiddenIsFalse()
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

    override fun newPunishment(
        authenticatedUser: AuthenticatedUser,
        request: PunishmentCreationRequest,
    ): PunishmentCreationResponse {
        val server = serverRepository.findByIdOrNull(request.server)
            ?: throw PortierException(ErrorCode.UNKNOWN_SERVER)
        val target = actorRepository.findByIdOrNull(request.target)
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR)
        val punisher = actorRepository.findByIdOrNull(request.punisher)
            ?: throw PortierException(ErrorCode.UNKNOWN_ACTOR)

        val punishment = PunishmentEntity(
            id = UUID.randomUUID(),
            server = server,
            target = target,
            punisher = punisher,
            creator = authenticatedUser.principal,
            lifted = false,
            hidden = false,
        )
        val saved = punishmentRepository.save(punishment)
        return PunishmentCreationResponse(saved.id)
    }
}
