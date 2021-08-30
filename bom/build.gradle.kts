plugins {
    `java-platform`
    id("portier.base-conventions")
}

indra {
    configurePublications {
        from(components["javaPlatform"])
    }
}

dependencies {
    constraints {
        sequenceOf<String>(
        ).forEach {
            api(project(":portier-$it"))
        }
    }
}
