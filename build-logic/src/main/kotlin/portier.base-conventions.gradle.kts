plugins {
    id("net.kyori.indra.publishing")
}

indra {
    javaVersions {
        target(11)
    }

    github("Discord-Portier", "portier-server") {
        ci(true)
    }
    // TODO(Mariell Hoversholm): Decide upon licence

    configurePublications {
        pom {
            developers {
                // Sort these alphabetically by ID, please.
                developer {
                    id.set("gdude")
                }

                developer {
                    id.set("Proximyst")
                    name.set("Mariell Hoversholm")
                    timezone.set("Europe/Stockholm")
                }

                developer {
                    id.set("rtm516")
                }

                developer {
                    id.set("Tim203")
                }
            }
        }
    }
}
