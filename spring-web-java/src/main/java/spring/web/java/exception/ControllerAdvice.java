package spring.web.java.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.dto.response.ApiResponse;

import java.util.List;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ApiResponse<Void> handler(CommonException commonException) {
        return new ApiResponse<>(commonException.getApiResponseCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handler(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        return new ApiResponse<>(ApiResponseCode.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handler(MethodArgumentNotValidException methodArgumentNotValidException) {
        return new ApiResponse<>(
                ApiResponseCode.PARAMETER_NOT_VALID,
                methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getField)
                        .toList()
        );
    }

    @ExceptionHandler
    public ApiResponse<Void> handler(JwtException jwtException) {
        if (jwtException instanceof UnsupportedJwtException) {
            return new ApiResponse<>(ApiResponseCode.UNSUPPORTED_TOKEN);
        } else if (jwtException instanceof ExpiredJwtException) {
            return new ApiResponse<>(ApiResponseCode.EXPIRED_TOKEN);
        } else {
            return new ApiResponse<>(ApiResponseCode.BAD_TOKEN);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handler(Exception exception) {
        return new ApiResponse<>(ApiResponseCode.SYSTEM_ERROR);
    }
}
