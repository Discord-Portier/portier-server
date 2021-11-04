package io.github.discordportier.server.annotation.security

import io.github.discordportier.server.model.auth.UserPermission

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PermissionRequired(vararg val permissions: UserPermission)
