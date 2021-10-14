package com.github.discordportier.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.web.server.WebExceptionHandler
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.violations.ConstraintViolationProblemModule

@Configuration
class ZalandoProblemConfig {
    @Bean
    fun problemModule(): ProblemModule = ProblemModule()

    @Bean
    fun constraintViolationProblemModule(): ConstraintViolationProblemModule = ConstraintViolationProblemModule()

    @Bean
    @Order(-2) // Must be before standard web handling
    fun problemExceptionHandler(mapper: ObjectMapper, problemHandling: ProblemHandling): WebExceptionHandler =
        ProblemExceptionHandler(mapper, problemHandling)
}
