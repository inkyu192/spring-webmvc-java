package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.model.entity.Member

interface MemberJpaRepository : JpaRepository<Member, Long> {

    fun findByAccount(account: String): Member?

    fun existsByAccount(account: String): Boolean
}