package com.webmvc.javaapi.exception;

import com.webmvc.javaapi.constant.ApiResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;

    public CommonException(ApiResponseCode apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }
}
