package io.github.discordportier.server.service

import io.github.discordportier.server.model.database.user.UserRepository
import com.google.common.hash.Hashing
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlin.random.Random
import org.apache.commons.lang3.RandomStringUtils

class UserAuthenticationServiceTest(
    private val userRepository: UserRepository = mockk(),
    private val userAuthenticationService: UserAuthenticationService =
        UserAuthenticationService(userRepository)
) : StringSpec({
    "Password should be hashed with SHA-512 with HMAC" {
        // We want to make sure stored passwords never change.
        // This test should only change if we do some kind of migration to another algorithm if it also updates
        //   the database properly. As the passwords are hashed, that should not be possible, and this is therefore
        //   a set in stone test.

        val salt = Random.nextBytes(64)
        val password = RandomStringUtils.random(64)
        val expected = Hashing.hmacSha512(salt).hashString(password, Charsets.UTF_8).asBytes()

        userAuthenticationService.hashPassword(password, salt) shouldBe expected
    }
})
