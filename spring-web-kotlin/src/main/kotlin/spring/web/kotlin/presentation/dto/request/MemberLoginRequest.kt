package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Email

data class MemberLoginRequest(
    @field:Email
    val account: String,
    val password: String,
)
