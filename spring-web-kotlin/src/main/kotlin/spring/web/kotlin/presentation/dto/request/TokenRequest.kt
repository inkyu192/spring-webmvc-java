package spring.web.kotlin.presentation.dto.request

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String,
)
