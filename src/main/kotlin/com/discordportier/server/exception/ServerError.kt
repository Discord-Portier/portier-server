package com.discordportier.server.exception

import com.discordportier.server.model.rest.v1.response.ErrorCode

interface ServerError {
    val errorCode: ErrorCode
    val errorDetail: String
}
