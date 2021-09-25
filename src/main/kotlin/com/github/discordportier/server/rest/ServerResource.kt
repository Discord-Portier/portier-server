package com.github.discordportier.server.rest

import com.github.discordportier.server.annotation.ProduceJson
import com.github.discordportier.server.annotation.security.Authenticated
import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.model.api.response.ServerListResponse
import com.github.discordportier.server.model.auth.UserPermission
import com.github.discordportier.server.model.database.server.ServerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/server")
@ProduceJson
@Authenticated
class ServerResource(
    private val serverRepository: ServerRepository,
) {
    @GetMapping("/list")
    @PermissionRequired(UserPermission.READ_SERVERS)
    fun list(): ServerListResponse =
        ServerListResponse(serverRepository.findAll().map {
            ServerListResponse.ServerEntry(
                it.id,
                it.name,
            )
        })
}
