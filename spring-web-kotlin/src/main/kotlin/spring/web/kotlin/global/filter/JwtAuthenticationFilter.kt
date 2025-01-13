package spring.web.kotlin.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import spring.web.kotlin.global.config.JwtTokenProvider
import spring.web.kotlin.global.config.security.UserDetailsImpl

class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        getAccessToken(request)
            ?.let { jwtTokenProvider.parseAccessToken(it) }
            ?.let { UserDetailsImpl(it) }
            ?.let { UsernamePasswordAuthenticationToken(it, it.password, it.authorities) }
            ?.also { SecurityContextHolder.getContext().authentication = it }

        chain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest) =
        request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
}