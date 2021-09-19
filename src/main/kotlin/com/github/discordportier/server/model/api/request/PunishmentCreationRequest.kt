package com.github.discordportier.server.model.api.request

data class PunishmentCreationRequest(
    val target: Long,
    val server: Long,
    val punisher: Long,
)
