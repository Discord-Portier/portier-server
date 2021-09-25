package com.github.discordportier.server.model.database.user

import com.github.discordportier.server.ext.now
import com.github.discordportier.server.model.auth.UserPermission
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

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

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
