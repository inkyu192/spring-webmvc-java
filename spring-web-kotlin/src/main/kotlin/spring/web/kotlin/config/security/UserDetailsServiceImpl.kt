package spring.web.kotlin.config.security

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spring.web.kotlin.repository.MemberRepository

class UserDetailsServiceImpl(
    private val memberRepository: MemberRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String) =
        memberRepository.findByAccount(username).orElse(null)
            ?.let { UserDetailsImpl(it) }
            ?: throw UsernameNotFoundException("유저 없음")
}