package io.github.discordportier.server.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class ZonedDateTimeConverter : Converter<String, ZonedDateTime> {
    override fun convert(source: String): ZonedDateTime = ZonedDateTime.parse(source)
}
