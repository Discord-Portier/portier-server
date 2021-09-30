package com.github.discordportier.server.model.database.punishment

import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.database.actor.ActorEntity
import com.github.discordportier.server.model.database.server.ServerEntity
import com.github.discordportier.server.model.database.user.UserEntity
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

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

    @Column(nullable = false)
    val lifted: Boolean,

    @Column(nullable = false)
    val hidden: Boolean,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
