package spring.web.kotlin.domain.repository

import spring.web.kotlin.domain.model.entity.Member

interface MemberRepository {
    fun findByIdOrNull(id: Long): Member?
    fun findByAccount(account: String): Member?
    fun save(member: Member): Member
    fun deleteById(id: Long)
}