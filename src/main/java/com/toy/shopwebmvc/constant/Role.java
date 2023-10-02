package com.toy.shopwebmvc.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자");

    private final String description;
}
