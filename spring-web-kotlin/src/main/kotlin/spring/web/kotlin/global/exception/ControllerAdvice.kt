package spring.web.kotlin.global.exception

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler
    fun handler(exception: DomainException) =
        ProblemDetail.forStatus(exception.httpStatus).apply {
            title = exception.responseMessage.title
            detail = exception.responseMessage.detail
        }

    @ExceptionHandler
    fun handler(exception: HttpRequestMethodNotSupportedException) =
        ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED).apply {
            title = HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler
    fun handler(exception: MethodArgumentNotValidException) =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = HttpStatus.BAD_REQUEST.reasonPhrase
            detail = "${exception.message}: ${exception.bindingResult.fieldErrors.map { it.field }.toList()}"
        }

    @ExceptionHandler
    fun handler(exception: JwtException) =
        ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED).apply {
            title = HttpStatus.UNAUTHORIZED.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler
    fun handler(exception: Exception) =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            title = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            detail = exception.message
        }
}