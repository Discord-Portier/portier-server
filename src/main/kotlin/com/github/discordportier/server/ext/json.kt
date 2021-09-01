package com.github.discordportier.server.ext

import com.github.discordportier.server.ktxser.ZonedDateTimeSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val jsonSerializer: Json = Json {
    serializersModule = SerializersModule {
        contextual(ZonedDateTimeSerializer)
    }
}
