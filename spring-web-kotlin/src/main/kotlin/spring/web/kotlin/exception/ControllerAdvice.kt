package spring.web.kotlin.exception

import org.slf4j.LoggerFactory
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.dto.response.ApiResponse

@RestControllerAdvice
class ControllerAdvice {

    private val log = LoggerFactory.getLogger(ControllerAdvice::class.java)

    @ExceptionHandler
    fun handler(commonException: CommonException): ApiResponse<Void> {
        return ApiResponse(commonException.apiResponseCode)
    }

    @ExceptionHandler
    fun handler(methodArgumentNotValidException: MethodArgumentNotValidException): ApiResponse<List<String>> {
        return ApiResponse(
            ApiResponseCode.PARAMETER_NOT_VALID,
            methodArgumentNotValidException.bindingResult.fieldErrors.map(FieldError::getField))
    }

    @ExceptionHandler
    fun handler(exception: Exception): ApiResponse<Void> {
        log.error("[ExceptionHandler]", exception)

        return ApiResponse(ApiResponseCode.SYSTEM_ERROR)
    }
}