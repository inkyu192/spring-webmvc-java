package spring.web.kotlin.infrastructure.util

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityContextUtil {
    fun getMemberId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            throw BadCredentialsException("인증되지 않은 사용자입니다.")
        }

        return authentication.principal.toString().toLong()
    }
}