package spring.web.java.dto.request;

import jakarta.validation.constraints.NotNull;
import spring.web.java.common.constant.Role;

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
