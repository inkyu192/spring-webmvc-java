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
        getAccessToken(request)
            ?.let { token: String ->
                val claims = jwtTokenProvider.parseAccessToken(token)
                UsernamePasswordAuthenticationToken(
                    claims["memberId"],
                    token,
                    listOf(SimpleGrantedAuthority(claims["role"].toString()))
                )
            }
            ?.also { SecurityContextHolder.getContext().authentication = it }

        filterChain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest) =
        request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
}