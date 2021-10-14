package com.github.discordportier.server.exception

import com.github.discordportier.server.model.api.response.ErrorCode
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import org.zalando.problem.Problem
import org.zalando.problem.StatusType
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import reactor.core.publisher.Mono

private val klogger = KotlinLogging.logger { }

@ControllerAdvice
class ExceptionAdvice : ProblemHandling {
    @ExceptionHandler
    suspend fun unhandledException(exception: Exception, webRequest: ServerWebExchange): ResponseEntity<Problem> =
        handleException(
            exception,
            webRequest,
            ErrorCode.UNKNOWN,
        )

    @ExceptionHandler
    suspend fun handleBase(exception: PortierException, webRequest: ServerWebExchange): ResponseEntity<Problem> =
        handleException(
            exception,
            webRequest,
            exception.errorCode,
            springStatus = exception.httpStatus,
        )

    @ExceptionHandler(value = [AuthenticationException::class, AccessDeniedException::class])
    suspend fun handleAuthException(exception: Exception, webRequest: ServerWebExchange): ResponseEntity<Problem> =
        handleException(
            exception,
            webRequest,
            ErrorCode.UNAUTHORIZED,
        )

    private suspend fun handleException(
        exception: Exception,
        webRequest: ServerWebExchange,
        errorCode: ErrorCode,
        springStatus: HttpStatus = errorCode.http,
        zalandoStatus: StatusType = springStatus.zalandoStatus,
    ): ResponseEntity<Problem> {
        val problem = prepare(exception, zalandoStatus, Problem.DEFAULT_TYPE)
            .with("error_code", errorCode)
            .build()
        problem.stackTrace = createStackTrace(exception)
        log(exception, problem, webRequest, springStatus).awaitSingleOrNull()
        return ResponseEntity.status(springStatus).body(problem)
    }

    override fun log(
        throwable: Throwable,
        problem: Problem,
        request: ServerWebExchange,
        status: HttpStatus
    ): Mono<Void?> = mono {
        if (status.is4xxClientError) {
            klogger.warn(throwable) { "Client error in request. ${status.value()} ${status.reasonPhrase}" }
        } else if (status.is5xxServerError) {
            klogger.error(throwable) { "Server error in request. ${status.value()} ${status.reasonPhrase}" }
        }

        null
    }
}
