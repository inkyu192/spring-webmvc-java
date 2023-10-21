package com.toy.shopwebmvc.dto.request;

import com.toy.shopwebmvc.constant.Role;
import jakarta.validation.constraints.NotNull;

public record MemberUpdateRequest(
        @NotNull(message = "필수 값")
        String name,
        @NotNull(message = "필수 값")
        Role role,
        @NotNull(message = "필수 값")
        String city,
        @NotNull(message = "필수 값")
        String street,
        @NotNull(message = "필수 값")
        String zipcode
) {
}
