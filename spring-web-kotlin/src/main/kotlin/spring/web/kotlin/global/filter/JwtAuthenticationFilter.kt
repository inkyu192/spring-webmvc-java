package spring.web.kotlin.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import spring.web.kotlin.global.config.JwtTokenProvider

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractToken(request)

        if (!token.isNullOrBlank()) {
            SecurityContextHolder.getContext().authentication = generateAuthentication(token)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest) =
        request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")

    private fun generateAuthentication(token: String) =
        jwtTokenProvider.parseAccessToken(token).let {
            UsernamePasswordAuthenticationToken(
                it["memberId"],
                token,
                listOf(SimpleGrantedAuthority(it["role"].toString()))
            )
        }
}
