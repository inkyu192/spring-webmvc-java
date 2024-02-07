package spring.web.kotlin.dto.response

import spring.web.kotlin.constant.Role
import spring.web.kotlin.domain.Member

data class MemberResponse(
    val id: Long,
    val account: String,
    val name: String,
    val city: String,
    val street: String,
    val zipcode: String,
    val role: Role
) {
    constructor(member: Member) : this(
        id = member.id!!,
        account = member.account,
        name = member.name,
        city = member.address.city,
        street = member.address.street,
        zipcode = member.address.zipcode,
        role = member.role
    )
}
