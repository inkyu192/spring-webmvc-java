package spring.web.kotlin.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import spring.web.kotlin.config.security.JwtTokenProvider
import spring.web.kotlin.config.security.UserDetailsImpl

class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        getAccessToken(request)
            ?.let {
                val claims = jwtTokenProvider.parseAccessToken(it)
                val userDetails = UserDetailsImpl(claims)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.password,
                    userDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authentication
            }

        chain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest) = request.getHeader(HttpHeaders.AUTHORIZATION)
        .takeIf { it.startsWith("Bearer") }
        ?.replace("Bearer ", "")
}