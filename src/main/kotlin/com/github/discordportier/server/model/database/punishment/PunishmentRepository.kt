package com.github.discordportier.server.model.database.punishment

import com.github.discordportier.server.model.database.server.ServerEntity
import java.util.UUID
import java.util.stream.Stream
import org.springframework.data.repository.CrudRepository

interface PunishmentRepository : CrudRepository<PunishmentEntity, UUID> {
    fun findAllByHiddenIsFalse(): Iterable<PunishmentEntity>

    fun streamAllByServerEquals(server: ServerEntity): Stream<PunishmentEntity>
}
