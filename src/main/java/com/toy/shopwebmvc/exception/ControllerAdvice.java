package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.dto.response.ApiResponse;
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
    public ApiResponse<Void> CommonExceptionHandler(CommonException e) {
        ApiResponseCode apiResponseCode = e.getApiResponseCode();

        return new ApiResponse<>(apiResponseCode.getCode(), apiResponseCode.getMessage(), null);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ArrayList<String> errors = new ArrayList<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getField());
        }

        return new ApiResponse<>(BAD_REQUEST.getCode(), BAD_REQUEST.getMessage(), errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> ExceptionHandler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return new ApiResponse<>(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage(), null);
    }
}
