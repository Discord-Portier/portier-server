package com.github.discordportier.server.model.database.actor

import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.database.user.UserEntity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
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

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
