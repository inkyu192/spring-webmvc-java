package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;

    public CommonException(ApiResponseCode apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }
}
