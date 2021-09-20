package com.github.discordportier.server.annotation

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping

/**
 * A helper annotation for consuming [JSON][MediaType.APPLICATION_JSON_VALUE].
 *
 * This is necessary because Spring's `GET` methods inherit any `Content-Type` requirements on a type-level.
 * This is fine for `produces`, but not `consumes`.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequestMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
annotation class ConsumeJson

/**
 * A helper annotation for producing [JSON][MediaType.APPLICATION_JSON_VALUE].
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
annotation class ProduceJson
