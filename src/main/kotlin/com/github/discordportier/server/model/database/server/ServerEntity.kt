package com.github.discordportier.server.model.database.server

import com.github.discordportier.server.ext.now
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = "portier", name = "servers")
class ServerEntity(
    @Id
    val id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val trusted: Boolean,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
