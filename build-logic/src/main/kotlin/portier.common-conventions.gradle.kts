import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("portier.base-conventions")
    id("net.kyori.indra")
    id("net.kyori.indra.license-header")
    id("com.adarshr.test-logger")
    id("org.checkerframework")
    jacoco
}

testlogger {
    theme = ThemeType.MOCHA_PARALLEL
    showPassed = false
}

configurations {
    testCompileClasspath {
        // group = junit is JUnit 4.
        exclude(group = "junit")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(enforcedPlatform("org.junit:junit-bom:${JUNIT_JUPITER_VERSION}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
}
