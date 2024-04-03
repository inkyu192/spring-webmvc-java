package spring.web.java.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.web.java.exception.CommonException;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("어드민"),
    ROLE_BUYER("소비자"),
    ROLE_SELLER("판매자");

    private final String description;

    public static Role of(Object name) {
        for (Role role : Role.values()) {
            if (role.name().equals(name)) {
                return role;
            }
        }

        throw new CommonException(ApiResponseCode.BAD_REQUEST);
    }
}
