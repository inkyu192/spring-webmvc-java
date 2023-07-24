package com.toy.shop.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import static com.toy.shop.common.ApiResponseCode.SUCCESS;

@Getter
@ToString
public class ApiResponseDto<T> {

    private final String resultCode;
    private final String resultMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiResponseDto() {
        this.resultCode = SUCCESS.getCode();
        this.resultMessage = SUCCESS.getMessage();
        this.data = null;
    }

    public ApiResponseDto(T data) {
        this.resultCode = SUCCESS.getCode();
        this.resultMessage = SUCCESS.getMessage();
        this.data = data;
    }

    public ApiResponseDto(String resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }
}
