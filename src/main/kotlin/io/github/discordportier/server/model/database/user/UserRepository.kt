package io.github.discordportier.server.model.database.user

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun getByIdEquals(uuid: UUID): UserEntity?
}
