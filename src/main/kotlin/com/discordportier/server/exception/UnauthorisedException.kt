package com.discordportier.server.exception

import com.discordportier.server.model.authentication.UserPermission
import com.discordportier.server.model.rest.v1.response.ErrorCode

class UnauthorisedException(vararg permissions: UserPermission) : Exception(
    "Missing permissions: ${permissions.joinToString(", ")}",
), ServerError {
    override val errorCode = ErrorCode.UNAUTHORISED
    override val errorDetail = "You are missing permissions"
    override val properties: Map<String, Any> = mapOf("permissions" to permissions)
}
