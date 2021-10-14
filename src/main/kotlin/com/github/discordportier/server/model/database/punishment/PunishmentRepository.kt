package com.github.discordportier.server.model.database.punishment

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface PunishmentRepository : CrudRepository<PunishmentEntity, UUID> {
    fun findAllByHiddenIsFalse(): Iterable<PunishmentEntity>
}
