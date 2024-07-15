package spring.web.java.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.dto.response.ApiResponse;
import spring.web.java.filter.HttpLogFilter;

import java.util.List;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final ServletRequest servletRequest;

    @ExceptionHandler
    public ApiResponse<Void> handler(CommonException e) {
        return new ApiResponse<>(e.getApiResponseCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<List<String>> handler(HttpRequestMethodNotSupportedException e) {
        return new ApiResponse<>(ApiResponseCode.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handler(MethodArgumentNotValidException e) {
        return new ApiResponse<>(
                ApiResponseCode.PARAMETER_NOT_VALID,
                e.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getField)
                        .toList()
        );
    }

    @ExceptionHandler
    public ApiResponse<Void> handler(JwtException e) {
        throw e;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handler(Exception e) {
        log.error("[{}]", servletRequest.getAttribute(HttpLogFilter.TRANSACTION_ID), e);

        return new ApiResponse<>(ApiResponseCode.SYSTEM_ERROR);
    }
}
