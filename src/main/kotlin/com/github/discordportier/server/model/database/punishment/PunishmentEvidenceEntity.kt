package com.github.discordportier.server.model.database.punishment

import com.github.discordportier.server.ext.now
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
@Table(schema = "portier", name = "punishment_evidence")
class PunishmentEvidenceEntity(
    @Id
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "punishment_id", nullable = false)
    val punishment: PunishmentEntity,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserEntity,

    @Column(nullable = false)
    val value: String,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)