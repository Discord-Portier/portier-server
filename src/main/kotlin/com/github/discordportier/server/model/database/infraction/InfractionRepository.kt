package com.github.discordportier.server.model.database.infraction

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface InfractionRepository : CrudRepository<InfractionEntity, UUID> {
    fun findAllByHiddenIsFalse(): Iterable<InfractionEntity>
}
