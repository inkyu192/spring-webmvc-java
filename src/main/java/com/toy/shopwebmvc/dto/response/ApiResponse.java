package com.toy.shopwebmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.toy.shopwebmvc.constant.ApiResponseCode.OK;

public record ApiResponse<T>(
        String resultCode,
        String resultMessage,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data
) {
    public ApiResponse() {
        this(OK.getCode(), OK.getMessage(), null);
    }

    public ApiResponse(T data) {
        this(OK.getCode(), OK.getMessage(), data);
    }

    public ApiResponse(String code, String message) {
        this(code, message, null);
    }
}
