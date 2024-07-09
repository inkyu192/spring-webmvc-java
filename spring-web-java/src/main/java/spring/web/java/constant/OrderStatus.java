package spring.web.java.constant;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    CANCEL("취소");

    private final String description;

    @JsonCreator
    public static OrderStatus of(Object name) {
        return Arrays.stream(OrderStatus.values())
            .filter(orderStatus -> orderStatus.name().equals(name))
            .findFirst()
            .orElse(null);
    }
}
