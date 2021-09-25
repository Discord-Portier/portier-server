package com.github.discordportier.server.model.database.punishment

import com.github.discordportier.server.model.database.server.ServerEntity
import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.stream.Stream

interface PunishmentRepository : CrudRepository<PunishmentEntity, UUID> {
    fun streamAllByServerEquals(server: ServerEntity): Stream<PunishmentEntity>
}
