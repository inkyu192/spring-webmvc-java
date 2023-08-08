package com.toy.shop.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.toy.shop.common.ApiResponseCode.SUCCESS;

public record ApiResponse<T>(
        String resultCode,
        String resultMessage,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data
) {
    public ApiResponse() {
        this(SUCCESS.getCode(), SUCCESS.getMessage(), null);
    }

    public ApiResponse(T data) {
        this(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }
}
