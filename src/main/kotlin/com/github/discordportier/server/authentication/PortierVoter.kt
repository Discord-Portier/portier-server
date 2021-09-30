package com.github.discordportier.server.authentication

import kotlin.reflect.KClass
import org.aopalliance.intercept.MethodInvocation
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication

abstract class PortierVoter<A : ConfigAttribute>(
    private val attributeType: KClass<A>,
    private val allowMultipleAttributes: Boolean = false,
) : AccessDecisionVoter<MethodInvocation> {
    private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()

    override fun supports(clazz: Class<*>): Boolean =
        MethodInvocation::class.java.isAssignableFrom(clazz)

    override fun supports(attribute: ConfigAttribute): Boolean =
        attributeType.java.isAssignableFrom(attribute.javaClass)

    final override fun vote(
        authentication: Authentication,
        invocation: MethodInvocation,
        attributes: MutableCollection<ConfigAttribute>
    ): Int {
        val filteredAttributes = attributes.filterIsInstanceTo(mutableSetOf(), this.attributeType.java)
        if (filteredAttributes.isEmpty()) {
            return ACCESS_ABSTAIN
        }
        require(allowMultipleAttributes || filteredAttributes.size == 1) { "Multiple ${attributeType.java.name} where only 1 is allowed" }
        return vote(authentication, invocation, filteredAttributes)
    }

    open fun vote(authentication: Authentication, invocation: MethodInvocation, attribute: A): Int =
        vote(authentication, invocation, setOf(attribute))

    open fun vote(authentication: Authentication, invocation: MethodInvocation, attributes: Set<A>): Int =
        vote(authentication, invocation, attributes.first())
}
