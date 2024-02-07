package spring.web.kotlin.repository.custom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.Member

interface MemberCustomRepository {

    fun findAllWithJpql(pageable: Pageable, account: String?, name: String?): Page<Member>
}