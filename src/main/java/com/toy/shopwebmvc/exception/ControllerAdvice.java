package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;



@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ApiResponse<Void> handler(CommonException e) {
        return new ApiResponse<>(e.getApiResponseCode());
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handler(JwtException e) {
        return new ApiResponse<>(ApiResponseCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return new ApiResponse<>(ApiResponseCode.SYSTEM_ERROR);
    }
}
