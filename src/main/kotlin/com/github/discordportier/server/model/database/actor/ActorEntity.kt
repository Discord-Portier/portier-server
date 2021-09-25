package com.github.discordportier.server.model.database.actor

import com.github.discordportier.server.model.database.user.UserEntity
import javax.persistence.*

@Entity
@Table(schema = "portier", name = "actors")
class ActorEntity(
    @Id
    val id: Long,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val discriminator: Int,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserEntity,
)
