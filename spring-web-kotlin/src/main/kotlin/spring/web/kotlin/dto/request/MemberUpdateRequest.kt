package spring.web.kotlin.dto.request

import spring.web.kotlin.constant.Role

data class MemberUpdateRequest(
    val name: String,
    val role: Role,
    val city: String,
    val street: String,
    val zipcode: String
)
