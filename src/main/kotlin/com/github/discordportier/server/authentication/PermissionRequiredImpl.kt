package com.github.discordportier.server.authentication

import com.github.discordportier.server.annotation.security.PermissionRequired
import com.github.discordportier.server.authentication.PermissionRequiredMetadataExtractor.PermissionRequiredAttribute
import com.github.discordportier.server.model.auth.AuthenticatedUser
import com.github.discordportier.server.model.auth.UserPermission
import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED
import org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.annotation.AnnotationMetadataExtractor
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class PermissionRequiredMetadataExtractor : AnnotationMetadataExtractor<PermissionRequired> {
    override fun extractAttributes(securityAnnotation: PermissionRequired): Collection<ConfigAttribute> =
        setOf(PermissionRequiredAttribute(securityAnnotation.permissions.toSet()))

    data class PermissionRequiredAttribute(
        val permissions: Set<UserPermission>,
    ) : ConfigAttribute {
        override fun getAttribute(): String = "PermissionRequired(permissions = $permissions)"
    }
}

@Component
class PermissionRequiredVoter : PortierVoter<PermissionRequiredAttribute>(PermissionRequiredAttribute::class) {
    override fun vote(
        authentication: Authentication,
        invocation: MethodInvocation,
        attribute: PermissionRequiredAttribute
    ): Int {
        if (authentication !is AuthenticatedUser) {
            return ACCESS_DENIED
        }

        if (attribute.permissions.all { authentication.principal.userPermissions.any { ent -> ent.permission == it } }) {
            return ACCESS_GRANTED
        }

        return ACCESS_DENIED
    }
}
