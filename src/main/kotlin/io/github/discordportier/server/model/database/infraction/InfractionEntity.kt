package io.github.discordportier.server.model.database.infraction

import io.github.discordportier.server.ext.now
import io.github.discordportier.server.model.database.actor.ActorEntity
import io.github.discordportier.server.model.database.server.ServerEntity
import io.github.discordportier.server.model.database.user.UserEntity
import io.github.discordportier.server.model.punishment.InfractionCategory
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(schema = "portier", name = "infractions")
class InfractionEntity(
    @Id
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    val target: ActorEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punisher_id", nullable = false)
    val punisher: ActorEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    val server: ServerEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserEntity,

    @Column(nullable = false)
    val lifted: Boolean,

    @Column(nullable = false)
    val hidden: Boolean,

    @Column(nullable = false)
    val reason: String,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val infractionCategory: InfractionCategory,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "infraction_id")
    val evidence: MutableSet<InfractionEvidenceEntity>,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
