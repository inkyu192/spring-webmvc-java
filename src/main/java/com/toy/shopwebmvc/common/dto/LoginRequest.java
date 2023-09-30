package com.toy.shopwebmvc.common.dto;

public record LoginRequest(
        String account,
        String password
) {
}
