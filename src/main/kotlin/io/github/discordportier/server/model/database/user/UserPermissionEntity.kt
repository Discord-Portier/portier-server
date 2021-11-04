package io.github.discordportier.server.model.database.user

import io.github.discordportier.server.ext.now
import io.github.discordportier.server.model.auth.UserPermission
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(schema = "portier", name = "user_permissions")
class UserPermissionEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val permission: UserPermission,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.EAGER) // Let this be eager as a permission is useless on its own without a user.
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserEntity,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
