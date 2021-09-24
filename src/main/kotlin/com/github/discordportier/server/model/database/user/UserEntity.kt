package com.github.discordportier.server.model.database.user

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "portier", name = "users")
class UserEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val username: String,

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
)
