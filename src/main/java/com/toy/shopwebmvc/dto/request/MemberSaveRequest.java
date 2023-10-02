package com.toy.shopwebmvc.dto.request;

import com.toy.shopwebmvc.constant.Role;
import jakarta.validation.constraints.NotEmpty;

public record MemberSaveRequest(
        @NotEmpty
        String account,
        @NotEmpty
        String password,
        @NotEmpty
        String name,
        Role role,
        @NotEmpty
        String city,
        @NotEmpty
        String street,
        @NotEmpty
        String zipcode
) {
}
