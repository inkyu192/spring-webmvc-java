package com.toy.shopwebmvc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.toy.shopwebmvc.constant.ApiResponseCode.OK;

public record ApiResponse<T>(
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T payload
) {
    public ApiResponse() {
        this(OK.getCode(), OK.getMessage());
    }

    public ApiResponse(T data) {
        this(OK.getCode(), OK.getMessage(), data);
    }

    public ApiResponse(String code, String message) {
        this(code, message, null);
    }
}
