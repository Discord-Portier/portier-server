plugins {
    id("portier.common-conventions")
}

dependencies {
    api(project(":portier-server-api"))
    api(rootProject.libs.jackson.annotations)
    api(rootProject.libs.jackson.databind)
}
