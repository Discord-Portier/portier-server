package com.github.discordportier.server.exception

import com.github.discordportier.server.model.api.response.ErrorCode
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.spring.web.advice.ProblemHandling

private val logger = KotlinLogging.logger { }

@ControllerAdvice
class ExceptionAdvice : ProblemHandling {
    @ExceptionHandler
    fun handleBase(exception: PortierException, webRequest: NativeWebRequest): ResponseEntity<Problem> {
        val problem = prepare(exception, exception.status, Problem.DEFAULT_TYPE)
            .with("error_code", exception.errorCode.name)
            .build()
        problem.stackTrace = createStackTrace(exception)
        log(exception, problem, webRequest, exception.errorCode.http)
        return ResponseEntity.status(exception.errorCode.http).body(problem)
    }

    @ExceptionHandler(value = [AuthenticationException::class, AccessDeniedException::class])
    fun handleAuthException(exception: Exception, webRequest: NativeWebRequest): ResponseEntity<Problem> {
        val problem = prepare(exception, HttpStatus.UNAUTHORIZED.zalandoStatus, Problem.DEFAULT_TYPE)
            .with("error_code", ErrorCode.UNAUTHORIZED)
            .build()
        problem.stackTrace = createStackTrace(exception)
        log(exception, problem, webRequest, HttpStatus.UNAUTHORIZED)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem)
    }

    override fun log(throwable: Throwable, problem: Problem, request: NativeWebRequest, status: HttpStatus) {
        if (status.is4xxClientError) {
            logger.warn(throwable) { "Client error in request. ${status.value()} ${status.reasonPhrase}" }
        } else if (status.is5xxServerError) {
            logger.error(throwable) { "Server error in request. ${status.value()} ${status.reasonPhrase}" }
        }
    }
}
