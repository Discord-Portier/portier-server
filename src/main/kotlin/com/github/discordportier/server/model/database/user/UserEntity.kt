package com.github.discordportier.server.model.database.user

import com.github.discordportier.server.ext.now
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    val creator: UserEntity?,

    @CreationTimestamp
    @Column(nullable = false)
    val created: ZonedDateTime = now(),

    @UpdateTimestamp
    @Column(nullable = false)
    val modified: ZonedDateTime = now(),
)
