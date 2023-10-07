package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final String code;
    private final String message;

    public CommonException(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.getCode(), apiResponseCode.getMessage());
    }

    public CommonException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
