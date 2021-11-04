package io.github.discordportier.server.ext

import java.time.ZoneOffset
import java.time.ZonedDateTime

fun now(): ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)

val ZonedDateTime.epochMilli: Long
    get() = toInstant().toEpochMilli()
