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

group = "io.github.discordportier.server"
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
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
        exclude("org.springframework.boot", "spring-boot-starter-webmvc")
    }

    implementation(libs.bundles.jackson)
    implementation(libs.logback)
    implementation(libs.kotlin.logging)
    implementation(libs.guava)
    implementation(libs.apache.commons.codec)
    implementation(libs.problem)
    implementation(libs.problem.jackson)
    implementation(libs.problem.spring)
    implementation(libs.bundles.springdoc)
    runtimeOnly(libs.postgresql)

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
