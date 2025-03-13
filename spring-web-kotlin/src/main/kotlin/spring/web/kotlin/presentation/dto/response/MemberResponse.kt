package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Member
import java.time.Instant
import java.time.LocalDate

data class MemberResponse(
    val id: Long,
    val account: String,
    val name: String,
    val phone: String,
    val birthDate: LocalDate,
    val createdAt: Instant,
) {
    constructor(member: Member) : this(
        id = checkNotNull(member.id),
        account = member.account,
        name = member.name,
        phone = member.phone,
        birthDate = member.birthDate,
        createdAt = member.createdAt,
    )
}
