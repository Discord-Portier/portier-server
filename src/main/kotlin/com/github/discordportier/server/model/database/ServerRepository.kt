package com.github.discordportier.server.model.database

import org.springframework.data.repository.CrudRepository

interface ServerRepository : CrudRepository<ServerEntity, Long>
