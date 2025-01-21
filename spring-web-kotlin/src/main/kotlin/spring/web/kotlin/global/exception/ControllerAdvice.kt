package spring.web.kotlin.global.exception

import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.web.kotlin.global.common.HttpLog
import spring.web.kotlin.global.filter.HttpLogFilter

@RestControllerAdvice
class ControllerAdvice(
    private val httpServletRequest: HttpServletRequest
) {
    private val log = LoggerFactory.getLogger(HttpLog::class.java)

    @ExceptionHandler(DomainException::class)
    fun domain(exception: DomainException) =
        ProblemDetail.forStatus(exception.httpStatus).apply {
            title = exception.responseMessage.title
            detail = exception.responseMessage.detail
        }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotAllowed(exception: HttpRequestMethodNotSupportedException) =
        ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED).apply {
            title = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun badRequest(exception: MethodArgumentNotValidException) =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = HttpStatus.BAD_REQUEST.reasonPhrase
            detail = "${exception.message}: ${exception.bindingResult.fieldErrors.map { it.field }.toList()}"
        }

    @ExceptionHandler(AuthenticationException::class, AuthorizationDeniedException::class, JwtException::class)
    fun unauthorized(exception: Exception) =
        ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED).apply {
            title = HttpStatus.UNAUTHORIZED.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler(Exception::class)
    fun internalServerError(exception: Exception): ProblemDetail {
        log.error("[{}]", httpServletRequest.getAttribute(HttpLogFilter.TRANSACTION_ID), exception)

        return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            title = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            detail = exception.message
        }
    }
}
