package com.github.discordportier.server.authentication

import org.springframework.core.GenericTypeResolver
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.annotation.AnnotationMetadataExtractor
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import kotlin.streams.asSequence

class PortierMetadataSources(
    private val annotationExtractors: Map<Class<Annotation>, AnnotationMetadataExtractor<*>>,
) : AbstractFallbackMethodSecurityMetadataSource() {
    constructor(annotationMetadataExtractors: Collection<AnnotationMetadataExtractor<*>>) : this(
        annotationMetadataExtractors.associateBy {
            checkNotNull(
                GenericTypeResolver.resolveTypeArgument(
                    it.javaClass,
                    AnnotationMetadataExtractor::class.java
                ) as Class<Annotation>?
            ) {
                "${it.javaClass.name} must specify a generic parameter for AnnotationMetadataExtractor"
            }
        }
    )

    override fun findAttributes(method: Method, targetClass: Class<*>): Collection<ConfigAttribute> =
        annotationExtractors
            .flatMap { processAnnotations(method, it) + processAnnotations(targetClass, it) }
            .toSet()

    override fun findAttributes(clazz: Class<*>): Collection<ConfigAttribute> =
        annotationExtractors
            .flatMap { processAnnotations(clazz, it) }
            .toList()

    override fun getAllConfigAttributes(): Collection<ConfigAttribute>? = null

    private fun processAnnotations(
        element: AnnotatedElement,
        entry: Map.Entry<Class<Annotation>, AnnotationMetadataExtractor<*>>
    ): Sequence<ConfigAttribute> =
        MergedAnnotations.from(element)
            .stream(entry.key)
            .asSequence()
            .flatMap {
                @Suppress("UNCHECKED_CAST") // The upper bound is Annotation
                (entry.value as AnnotationMetadataExtractor<Annotation>)
                    .extractAttributes(it.synthesize())
                    .asSequence()
            }
}
