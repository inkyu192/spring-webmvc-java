package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Member
import spring.web.kotlin.infrastructure.util.toOffsetDateTime
import java.time.OffsetDateTime

data class MemberResponse(
    val id: Long,
    val account: String,
    val name: String,
    val city: String,
    val street: String,
    val zipcode: String,
    val createdAt: OffsetDateTime,
) {
    constructor(member: Member) : this(
        id = member.id!!,
        account = member.account,
        name = member.name,
        city = member.address.city,
        street = member.address.street,
        zipcode = member.address.zipcode,
        createdAt = member.createdAt!!.toOffsetDateTime(),
    )
}
