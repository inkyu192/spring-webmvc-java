package spring.web.kotlin.domain.member.dto

import spring.web.kotlin.domain.member.Member


data class MemberUpdateRequest(
    val name: String,
    val role: Member.Role,
    val city: String,
    val street: String,
    val zipcode: String
)
