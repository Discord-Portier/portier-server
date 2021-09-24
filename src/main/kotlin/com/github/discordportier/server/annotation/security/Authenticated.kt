package com.github.discordportier.server.annotation.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("hasRole('USER')")
annotation class Authenticated
