package spring.web.kotlin.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

data class TokenResponse(
    val accessToken: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val refreshToken: String
)
