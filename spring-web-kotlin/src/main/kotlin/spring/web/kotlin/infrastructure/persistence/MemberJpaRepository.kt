package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import spring.web.kotlin.domain.model.entity.Member

interface MemberJpaRepository : JpaRepository<Member, Long> {

    @Query(
        """
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """
    )
    fun findByAccount(account: String): Member?
}