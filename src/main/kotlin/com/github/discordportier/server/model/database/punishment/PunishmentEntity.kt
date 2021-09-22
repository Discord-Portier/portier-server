package com.github.discordportier.server.model.database.punishment

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "portier", name = "punishments")
class PunishmentEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, name = "target_id")
    val targetId: Long,

    @Column(nullable = false, name = "punisher_id")
    val punisherId: Long,

    @Column(nullable = false, name = "server_id")
    val serverId: Long,
)
