package spring.web.kotlin.infrastructure.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
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
            SecurityContextHolder.getContext().authentication = createAuthentication(token)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest) =
        request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")

    private fun createAuthentication(token: String): Authentication {
        val claims = jwtTokenProvider.parseAccessToken(token)

        val memberId = claims["memberId"].toString().toLong()
        val permissions = claims["permissions", List::class.java]

        val authorities = permissions.map { SimpleGrantedAuthority(it as String) }

        return UsernamePasswordAuthenticationToken(memberId, token, authorities)
    }
}
