package io.github.discordportier.server.model.database.actor

import org.springframework.data.repository.CrudRepository

interface ActorRepository : CrudRepository<ActorEntity, Long>
