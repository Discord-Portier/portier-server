package com.github.discordportier.server.model.auth

import io.swagger.v3.oas.annotations.media.Schema

@Schema
enum class UserPermission {
    READ_PUNISHMENTS,
    WRITE_PUNISHMENTS,
    LIFT_PUNISHMENTS,
    DELETE_PUNISHMENTS,
    LIFT_OTHERS_PUNISHMENTS,
    DELETE_OTHERS_PUNISHMENTS,

    READ_SERVERS,
    WRITE_SERVERS,

    READ_ACTORS,
    CREATE_ACTORS,

    READ_USER_LIST,
    CREATE_USER,
    DELETE_USER,
    MODIFY_USER,
}
