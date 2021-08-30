plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.indra)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.shadow)
    application
    jacoco
}

group = "com.github.discordportier.server"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.log4j.impl)
    implementation(libs.bundles.jackson) // Required for log4j's config
    implementation(libs.kotlin.logging)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.kmongo)
    implementation(libs.kotlinx.cli)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

application {
    mainClass.set("com.discordportier.server.MainKt")
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
    shadowJar {
        archiveClassifier.set(null as String?)
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }
}
