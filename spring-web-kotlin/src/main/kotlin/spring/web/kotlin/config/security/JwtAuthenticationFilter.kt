package spring.web.kotlin.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        getAccessToken(request)
            ?.let(jwtTokenProvider::parseAccessToken)
            ?.let(::UserDetailsImpl)
            ?.let { UsernamePasswordAuthenticationToken(it, null, it.authorities) }
            ?.let { SecurityContextHolder.getContext().authentication = it }

        chain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest) = request.getHeader("Authorization")
        .takeIf { it.startsWith("Bearer") }
        ?.replace("Bearer ", "")
}