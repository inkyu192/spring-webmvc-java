package com.toy.shopwebmvc.dto.request;

import com.toy.shopwebmvc.constant.Role;

public record MemberUpdateRequest(
        String password,
        String name,
        Role role,
        String city,
        String street,
        String zipcode
) {
}
