package spring.web.kotlin.dto.request

import spring.web.kotlin.constant.Role

data class MemberSaveRequest(
    val account: String,
    val password: String,
    val name: String,
    val role: Role,
    val city: String,
    val street: String,
    val zipcode: String
)
