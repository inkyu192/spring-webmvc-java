package com.toy.shop.business.member.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record MemberSaveRequest(
        @NotEmpty
        String account,
        @NotEmpty
        String password,
        @NotEmpty
        String name,
        @NotEmpty
        String roleId,
        @NotEmpty
        String city,
        @NotEmpty
        String street,
        @NotEmpty
        String zipcode
) {
}
