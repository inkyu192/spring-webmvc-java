package spring.web.kotlin.presentation.dto.request

data class MemberLoginRequest(
    val account: String,
    val password: String
)
