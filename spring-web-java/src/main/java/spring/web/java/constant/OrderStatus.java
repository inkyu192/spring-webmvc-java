package spring.web.java.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    CANCEL("취소");

    private final String description;
}
