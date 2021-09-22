package com.github.discordportier.server.model.database.user

enum class UserPermission(val inheritedPermissions: Set<UserPermission> = emptySet()) {
    ADMINISTRATOR(values().toSet()),

    READ_PUNISHMENTS,
    WRITE_PUNISHMENTS,
    LIFT_PUNISHMENTS,
    DELETE_PUNISHMENTS,
    LIFT_OTHERS_PUNISHMENTS,
    DELETE_OTHERS_PUNISHMENTS,

    READ_SERVERS,
    WRITE_SERVERS,

    READ_USER_LIST,
    CREATE_USER,
    DELETE_USER,
    MODIFY_USER,
}
