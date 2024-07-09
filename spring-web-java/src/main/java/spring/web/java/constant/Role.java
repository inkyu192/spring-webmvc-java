package spring.web.java.constant;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자");

    private final String description;

    public static Role of(Object name) {
        return Arrays.stream(Role.values())
            .filter(role -> role.name().equals(name))
            .findFirst()
            .orElse(null);
    }
}
