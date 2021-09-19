package com.github.discordportier.server.model.database

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
)
