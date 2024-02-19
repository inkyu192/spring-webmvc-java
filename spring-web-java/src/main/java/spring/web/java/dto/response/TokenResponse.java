package spring.web.java.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record TokenResponse(
        String accessToken,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken
) {
}
