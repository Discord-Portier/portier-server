plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.depman)
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.indra)
    alias(libs.plugins.test.logger)
    jacoco
}

group = "com.github.discordportier.server"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.reactor)

    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.bundles.spring) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat") // We use Undertow
        exclude("org.springframework.boot", "spring-boot-starter-webmvc") // We use WebFlux
    }

    implementation(libs.guava)
    implementation(libs.apache.commons.codec)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.kotlin) // data classes etc...
    implementation(libs.jackson.yaml) // YAML application configuration
    implementation(libs.jackson.xml) // Logback configuration

    implementation(libs.logback)
    implementation(libs.kotlin.logging)

    implementation(libs.problem)
    implementation(libs.problem.jackson)
    implementation(libs.problem.spring)

    implementation(libs.springdoc.webflux)
    implementation(libs.springdoc.kotlin)

    implementation(libs.exposed.dao) // Kotlin database ORM
    implementation(libs.exposed.spring) // Spring Boot starter for Exposed
    implementation(libs.exposed.javatime) // `java.time` support
    implementation(libs.flyway) // Migrations; Exposed can't do them.
    runtimeOnly(libs.postgresql) // Only for SQL client driver

    testImplementation(libs.bundles.testing.api)
    testRuntimeOnly(libs.bundles.testing.runtime)
}

allOpen {
    // We want Hibernate to proxy certain classes.
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

indra {
    javaVersions {
        target(11)
    }

    github("Discord-Portier", "portier-server") {
        ci(true)
    }

    license {
        this.name("Mozilla Public License Version 2.0")
            .spdx("MPL-2.0")
            .url("https://www.mozilla.org/en-US/MPL/2.0/")
    }
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
    showPassed = false
}

configurations {
    testCompileClasspath {
        // group = junit is JUnit 4.
        exclude(group = "junit")
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }
}
