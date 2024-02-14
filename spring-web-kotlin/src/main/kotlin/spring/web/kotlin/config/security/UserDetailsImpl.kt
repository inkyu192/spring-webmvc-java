package spring.web.kotlin.config.security

import io.jsonwebtoken.Claims
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import spring.web.kotlin.domain.Member

class UserDetailsImpl private constructor(
    private val account: String,
    private val password: String,
    private val role: String
) : UserDetails {
    constructor(member: Member) : this(
        account = member.account,
        password = member.password,
        role = member.role.toString()
    )

    constructor(claims: Claims) : this(
        account = claims.subject,
        password = "",
        role = claims["authorities"].toString()
    )

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(role))

    override fun getPassword() = password

    override fun getUsername() = account

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}