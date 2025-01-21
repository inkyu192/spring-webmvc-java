package spring.web.kotlin.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import spring.web.kotlin.domain.member.Member

interface MemberRepository : JpaRepository<Member, Long> {

    @Query(
        """
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """
    )
    fun findByAccount(account: String): Member?
}