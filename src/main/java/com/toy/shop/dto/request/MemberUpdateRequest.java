package com.toy.shop.dto.request;

public record MemberUpdateRequest(
        String password,
        String name,
        String city,
        String street,
        String zipcode
) {
}
