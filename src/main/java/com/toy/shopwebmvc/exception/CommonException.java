package com.toy.shopwebmvc.exception;

import com.toy.shopwebmvc.common.ApiResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ApiResponseCode apiResponseCode;

    public CommonException(ApiResponseCode apiResponseCode) {
        this.apiResponseCode = apiResponseCode;
    }
}
