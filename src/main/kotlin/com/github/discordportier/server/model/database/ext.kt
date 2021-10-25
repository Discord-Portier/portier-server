package com.github.discordportier.server.model.database

import com.github.discordportier.server.ext.now
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

fun Table.modified(name: String = "modified") =
    timestamp(name).clientDefault { now().toInstant() }

fun Table.created(name: String = "created") =
    timestamp(name).clientDefault { now().toInstant() }

inline fun <reified T : Enum<T>> Table.enumByName(name: String) =
    enumerationByName(name, T::class.java.enumConstants!!.size, T::class)
