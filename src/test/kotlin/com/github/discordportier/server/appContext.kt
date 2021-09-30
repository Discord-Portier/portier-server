package com.github.discordportier.server

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class LocalContextLoads(
    private val applicationContext: ApplicationContext,
) : StringSpec({
    "local profile loads" {
        applicationContext.startupDate shouldNotBe null
    }
})
