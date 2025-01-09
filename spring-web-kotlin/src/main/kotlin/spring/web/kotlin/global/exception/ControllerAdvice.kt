package spring.web.kotlin.global.exception

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.web.kotlin.global.common.ApiResponseCode
import spring.web.kotlin.global.common.ApiResponse

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler
    fun handler(commonException: CommonException) = ApiResponse<Void>(commonException.apiResponseCode)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handler(e: HttpRequestMethodNotSupportedException) =
        ApiResponse<Void>(ApiResponseCode.BAD_REQUEST)

    @ExceptionHandler
    fun handler(e: MethodArgumentNotValidException) = ApiResponse(
        ApiResponseCode.PARAMETER_NOT_VALID,
        e.bindingResult.fieldErrors.map(FieldError::getField)
    )

    @ExceptionHandler
    fun handler(e: JwtException): ApiResponse<ApiResponseCode> = throw e

    @ExceptionHandler
    fun handler(exception: Exception) = ApiResponse<Void>(ApiResponseCode.SYSTEM_ERROR)
}