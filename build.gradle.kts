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
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.bundles.spring) {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
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

indra {
    javaVersions {
        target(11)
    }

    github("Discord-Portier", "portier-server") {
        ci(true)
    }
    // TODO(Mariell Hoversholm): Decide upon licence
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
