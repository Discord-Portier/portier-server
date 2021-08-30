import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("portier.common-conventions")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.kotlin.jvm")
}

tasks {
    shadowJar {
        archiveClassifier.set(null as String?)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            javaParameters = true
        }
    }
}
