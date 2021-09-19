package com.github.discordportier.server.model.database

import org.springframework.data.repository.CrudRepository
import java.util.*

interface PunishmentRepository : CrudRepository<PunishmentEntity, UUID>
