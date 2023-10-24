package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.dto.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shopwebmvc.constant.ApiResponseCode.BAD_REQUEST;
import static com.toy.shopwebmvc.constant.ApiResponseCode.INTERNAL_SERVER_ERROR;


@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ApiResponse<Void> commonExceptionHandler(CommonException e) {
        return new ApiResponse<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ArrayList<String> errors = new ArrayList<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getField());
        }

        return new ApiResponse<>(BAD_REQUEST.getCode(), BAD_REQUEST.getMessage(), errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> jwtExceptionHandler(JwtException e) {
        return new ApiResponse<>(BAD_REQUEST.getCode(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> exceptionHandler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return new ApiResponse<>(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage());
    }
}
