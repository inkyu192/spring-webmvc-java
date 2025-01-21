package spring.web.kotlin.domain.token.dto

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String
)
