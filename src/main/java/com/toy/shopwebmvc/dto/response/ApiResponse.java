package com.toy.shopwebmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toy.shopwebmvc.constant.ApiResponseCode;


public record ApiResponse<T>(
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T payload
) {
    public ApiResponse() {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), null);
    }

    public ApiResponse(T data) {
        this(ApiResponseCode.SUCCESS.name(), ApiResponseCode.SUCCESS.getMessage(), data);
    }

    public ApiResponse(ApiResponseCode apiResponseCode) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), null);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, T data) {
        this(apiResponseCode.name(), apiResponseCode.getMessage(), data);
    }

    public ApiResponse(ApiResponseCode apiResponseCode, String message) {
        this(apiResponseCode.name(), message, null);
    }
}
