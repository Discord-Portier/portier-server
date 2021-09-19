package com.github.discordportier.server.config

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.zalando.problem.jackson.ProblemModule

@Configuration
class JacksonConfig : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder) {
        jacksonObjectMapperBuilder.modules(
            KotlinModule(
                nullToEmptyMap = true,
                nullToEmptyCollection = true,
            ),
            JavaTimeModule(),
            ProblemModule(),
        )
        jacksonObjectMapperBuilder.featuresToDisable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
        )
        jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}
