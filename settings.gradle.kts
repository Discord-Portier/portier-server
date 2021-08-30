pluginManagement {
    includeBuild("build-logic")
}

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "portier-server"
includeProject("bom")
includeProject("api")
includeProject("server")

fun includeProject(path: String, name: String = "${rootProject.name}-${pathToProjectName(path, '-')}") {
    include(path)
    project(":${pathToProjectName(path)}").name = name
}

fun pathToProjectName(path: String, separator: Char = ':') =
    path.replace('/', separator)
        .replace('\\', separator)
