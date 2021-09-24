package com.github.discordportier.server.authentication

import org.aopalliance.intercept.MethodInvocation
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.AccessDecisionVoter.ACCESS_ABSTAIN
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication
import kotlin.reflect.KClass

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
        val attributes = findAttributes(attributes)
        if (attributes.isEmpty()) {
            return ACCESS_ABSTAIN
        }
        require(allowMultipleAttributes || attributes.size == 1) { "Multiple ${attributeType.java.name} where only 1 is allowed" }
        return vote(authentication, invocation, attributes)
    }

    open fun vote(authentication: Authentication, invocation: MethodInvocation, attribute: A): Int =
        vote(authentication, invocation, setOf(attribute))

    open fun vote(authentication: Authentication, invocation: MethodInvocation, attributes: Set<A>): Int =
        vote(authentication, invocation, attributes.first())

    private fun findAttributes(attributes: Collection<ConfigAttribute>): Set<A> =
        attributes.filterIsInstanceTo(mutableSetOf(), this.attributeType.java)
}
