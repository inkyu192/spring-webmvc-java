package spring.web.kotlin.presentation.exception.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.ErrorResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import spring.web.kotlin.presentation.exception.BaseException

@RestControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(exception: BaseException) = exception.body

    @ExceptionHandler(NoResourceFoundException::class, HttpRequestMethodNotSupportedException::class)
    fun handleResourceNotFound(errorResponse: ErrorResponse) = errorResponse.body

    @ExceptionHandler(HttpMessageNotReadableException::class, MethodArgumentTypeMismatchException::class)
    fun handleInvalidRequestBody(exception: Exception) =
        ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
            title = HttpStatus.BAD_REQUEST.reasonPhrase
            detail = exception.message
        }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exception: MethodArgumentNotValidException) =
        exception.body.apply {
            setProperty("fieldErrors", exception.bindingResult.fieldErrors.associate { it.field to it.defaultMessage })
        }

    @ExceptionHandler(ServletRequestBindingException::class)
    fun handleBindingException(exception: ServletRequestBindingException) = exception.body
}
