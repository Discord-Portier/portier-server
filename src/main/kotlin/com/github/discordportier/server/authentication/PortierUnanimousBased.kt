package com.github.discordportier.server.authentication

import mu.KotlinLogging
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.AccessDecisionVoter.*
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.vote.UnanimousBased
import org.springframework.security.core.Authentication

private val klogger = KotlinLogging.logger { }

class PortierUnanimousBased(
    voters: List<AccessDecisionVoter<out Any>>,
) : UnanimousBased(voters) {
    override fun getDecisionVoters(): List<AccessDecisionVoter<Any>> =
        // Kotlin makes this into List<AccessDecisionVoter<Nothing!>>; we want Any
        super.getDecisionVoters() as List<AccessDecisionVoter<Any>>

    override fun decide(
        authentication: Authentication,
        obj: Any,
        attributes: MutableCollection<ConfigAttribute>
    ) {
        klogger.debug { "Election over access decision called for ($obj) with attributes ($attributes)" }
        val grant = decisionVoters.count {
            when (val result = it.vote(authentication, obj, attributes)) {
                ACCESS_ABSTAIN -> false
                ACCESS_GRANTED -> true
                ACCESS_DENIED -> throw AccessDeniedException(
                    this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied.")
                )
                else -> throw IllegalStateException("Unknown decision ballot: $result from ${it.javaClass.name}")
            }
        }
        if (grant == 0) {
            checkAllowIfAllAbstainDecisions()
        }
    }
}
