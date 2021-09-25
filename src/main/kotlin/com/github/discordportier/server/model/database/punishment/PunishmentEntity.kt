package com.github.discordportier.server.model.database.punishment

import com.github.discordportier.server.model.database.actor.ActorEntity
import com.github.discordportier.server.model.database.server.ServerEntity
import com.github.discordportier.server.model.database.user.UserEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "portier", name = "punishments")
class PunishmentEntity(
    @Id
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    val target: ActorEntity,

    @ManyToOne
    @JoinColumn(name = "punisher_id", nullable = false)
    val punisher: ActorEntity,

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    val server: ServerEntity,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserEntity,
)
