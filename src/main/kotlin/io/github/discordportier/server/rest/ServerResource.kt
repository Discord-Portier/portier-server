package io.github.discordportier.server.rest

import io.github.discordportier.server.ext.io
import io.github.discordportier.server.model.api.response.ServerListResponse
import io.github.discordportier.server.model.database.server.ServerRepository
import io.github.discordportier.server.rest.definition.IServerResource
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerResource(
    private val serverRepository: ServerRepository,
) : IServerResource {
    override suspend fun list() =
        ServerListResponse(io { serverRepository.findAll() }.map {
            ServerListResponse.ServerEntry(
                it.id,
                it.name,
            )
        })
}
