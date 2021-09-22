package com.github.discordportier.server.model.database.punishment

import org.springframework.data.repository.CrudRepository
import java.util.*

interface PunishmentRepository : CrudRepository<PunishmentEntity, UUID>
