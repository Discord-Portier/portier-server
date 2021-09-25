package com.github.discordportier.server.model.api.response

data class ServerListResponse(
    val servers: Collection<ServerEntry>,
) {
    data class ServerEntry(
        val id: Long,
        val name: String,
    )
}
