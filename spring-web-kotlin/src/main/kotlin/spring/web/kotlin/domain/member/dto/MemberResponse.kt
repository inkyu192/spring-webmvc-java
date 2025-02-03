package spring.web.kotlin.domain.member.dto

import spring.web.kotlin.domain.member.Member
import spring.web.kotlin.global.common.toOffsetDateTime
import java.time.OffsetDateTime

data class MemberResponse(
    val id: Long,
    val account: String,
    val name: String,
    val city: String,
    val street: String,
    val zipcode: String,
    val role: Member.Role,
    val createdAt: OffsetDateTime,
) {
    constructor(member: Member) : this(
        id = member.id!!,
        account = member.account,
        name = member.name,
        city = member.address.city,
        street = member.address.street,
        zipcode = member.address.zipcode,
        role = member.role,
        createdAt = member.createdAt!!.toOffsetDateTime(),
    )
}
