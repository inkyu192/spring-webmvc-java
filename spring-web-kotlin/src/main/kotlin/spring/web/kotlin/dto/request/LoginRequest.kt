package spring.web.kotlin.dto.request

data class LoginRequest(
    val account: String,
    val password: String
)
