package com.github.discordportier.server.model.database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ActorTable : LongIdTable("actors") {
    val username = text("username")
    val discriminator = integer("discriminator")
    val creator = reference("creator_id", UserTable)
    val modified = modified()
    val created = created()
}

class ActorEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ActorEntity>(ActorTable)

    var username by ActorTable.username
    var discriminator by ActorTable.discriminator
    val creator by UserEntity referrersOn ActorTable.creator
    var modified by ActorTable.modified
    var created by ActorTable.created
}
