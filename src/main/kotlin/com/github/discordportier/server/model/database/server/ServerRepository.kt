package com.github.discordportier.server.model.database.server

import org.springframework.data.repository.CrudRepository

interface ServerRepository : CrudRepository<ServerEntity, Long>
