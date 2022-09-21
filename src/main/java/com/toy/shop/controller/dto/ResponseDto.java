package com.toy.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private String resultCode;
    private String resultMessage;
    private T data;

    public ResponseDto(T data) {
        this.resultCode = "200";
        this.resultMessage = "정상응답";
        this.data = data;
    }

    public ResponseDto(String resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }
}
