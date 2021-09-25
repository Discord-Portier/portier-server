package com.github.discordportier.server.model.database.user

import com.github.discordportier.server.ext.now
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "portier", name = "users")
class UserEntity(
    @Id
    val id: UUID,

    /**
     * SHA-512 hashed password.
     */
    @Column(nullable = false)
    val password: ByteArray,

    @Column(nullable = false)
    val salt: ByteArray,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val userPermissions: MutableSet<UserPermissionEntity>,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
