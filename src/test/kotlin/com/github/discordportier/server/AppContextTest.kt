package com.github.discordportier.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Nested
@ActiveProfiles("local")
class Local {
    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun ensureContextLoads() {
        assertThat(applicationContext).isNotNull
    }
}
