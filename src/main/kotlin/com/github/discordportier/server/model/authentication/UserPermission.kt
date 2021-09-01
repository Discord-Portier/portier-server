package com.github.discordportier.server.model.authentication

enum class UserPermission {
    MANAGE_USERS,

    INSERT_PUNISHMENTS_ON_OTHERS_BEHALF,
    DELETE_PUNISHMENTS_ON_OTHERS_BEHALF,

    INSERT_PUNISHMENTS,
    DELETE_PUNISHMENTS,

    READ_PUNISHMENTS,
}
