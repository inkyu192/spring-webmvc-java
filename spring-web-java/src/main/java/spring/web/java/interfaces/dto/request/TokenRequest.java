package spring.web.java.interfaces.dto.request;

import jakarta.validation.constraints.NotNull;

public record TokenRequest(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
