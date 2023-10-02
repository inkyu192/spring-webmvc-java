package com.toy.shopwebmvc.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    READY("준비"),
    COMP("완료");

    private final String description;
}
