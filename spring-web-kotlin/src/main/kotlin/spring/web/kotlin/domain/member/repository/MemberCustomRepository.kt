package spring.web.kotlin.domain.member.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.member.Member

interface MemberCustomRepository {

    fun findAllWithJpql(pageable: Pageable, account: String?, name: String?): Page<Member>
}