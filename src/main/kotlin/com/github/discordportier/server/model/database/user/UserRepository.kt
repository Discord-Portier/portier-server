package com.github.discordportier.server.model.database.user

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun getByIdEquals(uuid: UUID): UserEntity?
}
