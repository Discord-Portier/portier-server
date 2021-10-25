package com.github.discordportier.server.model.database

import com.github.discordportier.server.model.auth.UserPermission
import java.util.UUID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable

object UserTable : UUIDTable("users") {
    val password = binary("password")
    val salt = binary("salt")
    val creator = optReference("creator_id", UserTable)
    val modified = modified()
    val created = created()
}

object UserPermissionTable : UUIDTable("user_permissions") {
    val permission = enumByName<UserPermission>("permission")
    val user = reference("user_id", UserTable)
    val creator = optReference("creator_id", UserTable)
    val modified = modified()
    val created = created()
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var password by UserTable.password
    var salt by UserTable.salt
    var creator by UserEntity optionalReferencedOn UserTable.creator
    var modified by UserTable.modified
    var created by UserTable.created

    val permissions by UserPermissionEntity referrersOn UserPermissionTable.user
}

class UserPermissionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserPermissionEntity>(UserPermissionTable)

    var permission by UserPermissionTable.permission
    var user by UserEntity referencedOn UserPermissionTable.user
    var creator by UserEntity optionalReferencedOn UserPermissionTable.creator
    var modified by UserPermissionTable.modified
    var created by UserPermissionTable.created
}
