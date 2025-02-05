package spring.web.kotlin.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.ErrorResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(exception: DomainException) =
        ProblemDetail.forStatus(exception.httpStatus).apply {
            title = exception.responseMessage.title
            detail = exception.responseMessage.detail
        }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidRequestBody(exception: HttpMessageNotReadableException) =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = HttpStatus.BAD_REQUEST.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler(NoResourceFoundException::class, HttpRequestMethodNotSupportedException::class)
    fun handleResourceNotFound(errorResponse: ErrorResponse) = errorResponse.body

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exception: MethodArgumentNotValidException) =
        exception.body.apply {
            setProperty("fieldErrors", exception.bindingResult.fieldErrors.associate { it.field to it.defaultMessage })
        }
}
