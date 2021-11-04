package io.github.discordportier.server.model.api.websocket

import io.github.discordportier.server.model.api.response.ErrorCode

data class ErrorMessage(val code: ErrorCode) : IMessage
