plugins {
    id("portier.impl-conventions")
    application
}

dependencies {
    implementation(rootProject.libs.bundles.log4j.impl)
    implementation(rootProject.libs.kotlin.logging)
    implementation(rootProject.libs.bundles.jackson)
}

application {
    mainClass.set("com.discordportier.server.MainKt")
}
