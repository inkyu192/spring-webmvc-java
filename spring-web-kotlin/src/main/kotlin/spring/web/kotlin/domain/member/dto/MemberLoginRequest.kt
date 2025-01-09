package spring.web.kotlin.domain.member.dto

data class MemberLoginRequest(
    val account: String,
    val password: String
)
