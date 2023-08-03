package com.toy.shop.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {

    SUCCESS("200", "정상"),
    NOT_VALID("400", "요청 파라미터 오류"),
    ERROR("500", "시스템 오류가 발생하였습니다."),

    CATEGORY_NOT_FOUND("C0001", "카테고리가 존재하지 않습니다."),
    ITEM_NOT_FOUND("I0001", "아이템이 존재하지 않습니다."),
    ITEM_QUANTITY_NOT_ENOUGH("I0002", "아이템 수량이 부족합니다."),
    MEMBER_NOT_FOUND("M0001", "회원이 존재하지 않습니다."),
    ROLE_NOT_FOUND("R0001", "권한이 존재하지 않습니다."),
    ORDER_NOT_FOUND("O0001", "주문이 존재하지 않습니다."),
    ORDER_NOT_CANCEL("O0002", "이미 배송완료된 주문은 취소가 불가능합니다.");

    private final String code;
    private final String message;
}
