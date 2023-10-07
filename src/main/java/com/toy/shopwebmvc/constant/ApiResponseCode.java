package com.toy.shopwebmvc.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {

    OK("200", "정상"),
    DATA_DUPLICATE("200", "데이터 중복"),
    DATA_NOT_FOUND("200", "데이터 없음"),
    BAD_REQUEST("400", "요청 오류"),
    LOGIN_REQUIRED("400", "로그인 필요"),
    INTERNAL_SERVER_ERROR("500", "시스템 오류");

    private final String code;
    private final String message;
}
