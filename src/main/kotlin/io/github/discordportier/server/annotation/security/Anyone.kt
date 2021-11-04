package io.github.discordportier.server.annotation.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
annotation class Anyone
