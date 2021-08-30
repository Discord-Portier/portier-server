plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    // We can't use libs.versions.toml here, sadly.

    // https://plugins.gradle.org/plugin/net.kyori.indra
    val indraVersion = "2.0.6"
    // https://plugins.gradle.org/plugin/com.adarshr.test-logger
    val gradleTestLoggerVersion = "3.0.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    val kotlinVersion = "1.5.30"
    // https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow
    val shadowVersion = "7.0.0"
    // https://plugins.gradle.org/plugin/org.checkerframework
    val checkerFrameworkGradleVersion = "0.5.24"

    // indra-common includes license-header
    implementation("net.kyori:indra-common:$indraVersion")
    implementation("net.kyori:indra-publishing-sonatype:$indraVersion")
    implementation("com.adarshr:gradle-test-logger-plugin:$gradleTestLoggerVersion")
    implementation(kotlin("gradle-plugin", version = kotlinVersion))
    implementation(kotlin("serialization", version = kotlinVersion))
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:$shadowVersion")
    implementation("org.checkerframework:checkerframework-gradle-plugin:$checkerFrameworkGradleVersion")
}
