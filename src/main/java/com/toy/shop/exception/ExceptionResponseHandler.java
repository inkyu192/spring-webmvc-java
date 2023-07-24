package com.toy.shop.exception;

import com.toy.shop.common.ApiResponseCode;
import com.toy.shop.common.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.common.ApiResponseCode.ERROR;
import static com.toy.shop.common.ApiResponseCode.NOT_VALID;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(CommonException.class)
    public Object CommonExceptionHandler(CommonException e) {
        ApiResponseCode apiResponseCode = e.getApiResponseCode();

        return new ApiResponseDto<>(apiResponseCode.getCode(), apiResponseCode.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidExceptionHandler]", e);

        ArrayList<String> errors = new ArrayList<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getField());
        }

        return new ApiResponseDto<>(NOT_VALID.getCode(), NOT_VALID.getMessage(), errors);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Object ExceptionHandler(Exception e) {
        log.error("[ExceptionHandler]", e);

        return new ApiResponseDto<>(ERROR.getCode(), ERROR.getMessage(), null);
    }
}
