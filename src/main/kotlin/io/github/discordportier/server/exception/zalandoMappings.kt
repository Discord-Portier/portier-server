package io.github.discordportier.server.exception

import org.springframework.http.HttpStatus
import org.zalando.problem.StatusType

private val statusCache = HttpStatus.values().associateWith {
    object : StatusType {
        override fun getStatusCode(): Int = it.value()
        override fun getReasonPhrase(): String = it.reasonPhrase
    }
}

val HttpStatus.zalandoStatus: StatusType
    get() = checkNotNull(statusCache[this]) { "unmapped status?: $this" }
