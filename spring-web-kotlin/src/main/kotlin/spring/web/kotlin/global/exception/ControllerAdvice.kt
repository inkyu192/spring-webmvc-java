package spring.web.kotlin.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

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
}
