package com.github.discordportier.server.ext

import java.security.Principal
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast
import kotlinx.coroutines.reactor.awaitSingle
import reactor.core.publisher.Mono

suspend inline fun <reified P : Principal> Mono<Principal>.awaitSingleAs(): P = awaitSingleAs(P::class)
suspend fun <P : Principal> Mono<Principal>.awaitSingleAs(type: KClass<P>): P =
    type.safeCast(awaitSingle()) ?: throw IllegalStateException("Authenticated user is not of type ${type.java.name}")
