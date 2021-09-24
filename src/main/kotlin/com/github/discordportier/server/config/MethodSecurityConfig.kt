package com.github.discordportier.server.config

import com.github.discordportier.server.authentication.PortierMetadataSources
import com.github.discordportier.server.authentication.PortierUnanimousBased
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.annotation.AnnotationMetadataExtractor
import org.springframework.security.access.annotation.Jsr250Voter
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice
import org.springframework.security.access.method.MethodSecurityMetadataSource
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter
import org.springframework.security.access.vote.AuthenticatedVoter
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@Configuration
@AutoConfigureBefore(WebSecurityConfig::class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class MethodSecurityConfig(
    private val annotationMetadataExtractors: List<AnnotationMetadataExtractor<*>>,
    private val accessDecisionVoters: List<AccessDecisionVoter<*>>,
) : GlobalMethodSecurityConfiguration() {
    override fun customMethodSecurityMetadataSource(): MethodSecurityMetadataSource =
        PortierMetadataSources(annotationMetadataExtractors)

    override fun accessDecisionManager(): AccessDecisionManager {
        val decisionVoters = listOf(
            PreInvocationAuthorizationAdviceVoter(ExpressionBasedPreInvocationAdvice().also {
                it.setExpressionHandler(expressionHandler)
            }),
            Jsr250Voter(),
            RoleVoter(),
            AuthenticatedVoter(),
        ) + accessDecisionVoters
        val accessDecisionManager = PortierUnanimousBased(decisionVoters)
        accessDecisionManager.isAllowIfAllAbstainDecisions = true
        return accessDecisionManager
    }
}
