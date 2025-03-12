package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.model.entity.Member
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.infrastructure.persistence.MemberJpaRepository

@Repository
class MemberRepositoryAdapter(
    private val jpaRepository: MemberJpaRepository
) : MemberRepository {
    override fun findByIdOrNull(id: Long) = jpaRepository.findByIdOrNull(id)

    override fun findByAccount(account: String) = jpaRepository.findByAccount(account)

    override fun existsByAccount(account: String) = jpaRepository.existsByAccount(account)

    override fun save(member: Member) = jpaRepository.save(member)

    override fun delete(member: Member) {
        jpaRepository.delete(member)
    }
}