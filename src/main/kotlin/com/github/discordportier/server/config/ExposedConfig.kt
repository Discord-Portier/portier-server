package com.github.discordportier.server.config

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@AutoConfigureAfter(ExposedAutoConfiguration::class)
class ExposedConfig {
    @Bean
    @Primary
    fun transactionManager(springTransactionManager: SpringTransactionManager): SpringTransactionManager =
        springTransactionManager
}
