package com.webmvc.javaapi.dto.request;

import com.webmvc.javaapi.constant.Role;
import jakarta.validation.constraints.NotNull;

public record MemberSaveRequest(
        @NotNull
        String account,
        @NotNull
        String password,
        @NotNull
        String name,
        @NotNull
        Role role,
        @NotNull
        String city,
        @NotNull
        String street,
        @NotNull
        String zipcode
) {
}
