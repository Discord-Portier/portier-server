package com.github.discordportier.server.model.api.response

import org.springframework.http.HttpStatus
import org.springframework.web.socket.CloseStatus

enum class ErrorCode(
    val http: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val webSocket: CloseStatus = CloseStatus.SERVER_ERROR,
) {
    // Common
    UNIMPLEMENTED(http = HttpStatus.NOT_IMPLEMENTED, webSocket = CloseStatus.SERVER_ERROR),
    UNKNOWN(http = HttpStatus.INTERNAL_SERVER_ERROR, webSocket = CloseStatus.SERVER_ERROR),
    UNAUTHORIZED(http = HttpStatus.UNAUTHORIZED, webSocket = CloseStatus.POLICY_VIOLATION),

    // HTTP only
    UNKNOWN_SERVER(http = HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(http = HttpStatus.BAD_REQUEST),

    // WS only
    WEB_SOCKET_CANNOT_RECEIVE(webSocket = CloseStatus.NOT_ACCEPTABLE),
    WEB_SOCKET_ID_HIJACK_DISALLOWED(webSocket = CloseStatus.POLICY_VIOLATION),
    WEB_SOCKET_TRANSPORT_ERROR(webSocket = CloseStatus.SERVER_ERROR),
}
