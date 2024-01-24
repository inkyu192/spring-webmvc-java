package spring.web.kotlin.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.dto.response.ApiResponse

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler
    fun handler(commonException: CommonException) = ApiResponse<Void>(commonException.apiResponseCode)

    @ExceptionHandler
    fun handler(httpRequestMethodNotSupportedException: HttpRequestMethodNotSupportedException) =
        ApiResponse<Void>(ApiResponseCode.BAD_REQUEST)

    @ExceptionHandler
    fun handler(methodArgumentNotValidException: MethodArgumentNotValidException) = ApiResponse(
        ApiResponseCode.PARAMETER_NOT_VALID,
        methodArgumentNotValidException.bindingResult.fieldErrors.map(FieldError::getField)
    )

    @ExceptionHandler
    fun handler(exception: Exception) = ApiResponse<Void>(ApiResponseCode.SYSTEM_ERROR)
}