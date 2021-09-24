package com.github.discordportier.server.model.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority

enum class UserPermission : GrantedAuthority {
    ADMINISTRATOR,

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
    ;

    @JsonIgnore
    override fun getAuthority(): String = name
}
