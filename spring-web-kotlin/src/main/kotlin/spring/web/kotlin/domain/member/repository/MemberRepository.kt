package spring.web.kotlin.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import spring.web.kotlin.domain.member.Member
import java.util.Optional

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

    @Query(
        """
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """
    )
    fun findByAccount(account: String): Optional<Member>
}