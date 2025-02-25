package spring.web.kotlin.presentation.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
