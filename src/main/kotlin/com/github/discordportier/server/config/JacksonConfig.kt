package com.github.discordportier.server.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.jackson.JsonComponentModule
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.violations.ConstraintViolationProblemModule

@Configuration
class JacksonConfig(
    private val jsonComponentModule: JsonComponentModule,
    private val problemModule: ProblemModule,
    private val componentViolationProblemModule: ConstraintViolationProblemModule,
) : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder) {
        jacksonObjectMapperBuilder.modules(
            jsonComponentModule,
            KotlinModule(
                nullToEmptyMap = true,
                nullToEmptyCollection = true,
            ),
            JavaTimeModule(),
            problemModule,
            componentViolationProblemModule,
        )
        jacksonObjectMapperBuilder.featuresToDisable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        )
        jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}
