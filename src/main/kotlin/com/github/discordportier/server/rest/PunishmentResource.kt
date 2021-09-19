package com.github.discordportier.server.rest

import com.github.discordportier.server.exception.PortierException
import com.github.discordportier.server.model.api.request.PunishmentCreationRequest
import com.github.discordportier.server.model.api.response.ErrorCode
import com.github.discordportier.server.model.api.response.PunishmentCreationResponse
import com.github.discordportier.server.model.database.PunishmentEntity
import com.github.discordportier.server.model.database.PunishmentRepository
import com.github.discordportier.server.model.database.ServerEntity
import com.github.discordportier.server.model.database.ServerRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(
    "/v1/punishment",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class PunishmentResource(
    private val serverRepository: ServerRepository,
    private val punishmentRepository: PunishmentRepository,
) {
    @GetMapping("/list")
    fun listPunishments(): List<PunishmentEntity> = punishmentRepository.findAll().toList()

    @PostMapping("/new")
    fun newPunishment(@RequestBody request: PunishmentCreationRequest): PunishmentCreationResponse {
        if (!serverRepository.existsById(request.server)) {
            throw PortierException(ErrorCode.UNKNOWN_SERVER, "Server ${request.server} is unknown")
        }

        val punishment = PunishmentEntity(
            id = UUID.randomUUID(),
            targetId = request.target,
            serverId = request.server,
            punisherId = request.punisher,
        )
        val saved = punishmentRepository.save(punishment)
        return PunishmentCreationResponse(saved.id)
    }
}
