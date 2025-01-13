package spring.web.kotlin.global.config.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import spring.web.kotlin.domain.member.repository.MemberRepository
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.exception.DomainException

class UserDetailsServiceImpl(
    private val memberRepository: MemberRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val member = (memberRepository.findByAccount(username)
            ?: throw DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND))

        return UserDetailsImpl(member)
    }
}