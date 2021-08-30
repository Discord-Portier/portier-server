plugins {
    id("portier.impl-conventions")
    application
}

dependencies {
    api(project(":portier-server-api"))
    api(project(":portier-server-api-jackson"))
    implementation(rootProject.libs.bundles.log4j.impl)
    implementation(rootProject.libs.kotlin.logging)
    implementation(rootProject.libs.bundles.jackson)
    implementation(rootProject.libs.bundles.ktor.server)
}

application {
    mainClass.set("com.discordportier.server.MainKt")
}
