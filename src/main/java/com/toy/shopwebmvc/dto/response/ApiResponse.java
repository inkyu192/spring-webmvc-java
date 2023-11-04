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

    public ApiResponse(String code, String message) {
        this(code, message, null);
    }
}
