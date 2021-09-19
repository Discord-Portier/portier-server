package com.github.discordportier.server.model.api.websocket

import com.github.discordportier.server.model.api.response.ErrorCode

data class ErrorMessage(val code: ErrorCode) : IMessage
