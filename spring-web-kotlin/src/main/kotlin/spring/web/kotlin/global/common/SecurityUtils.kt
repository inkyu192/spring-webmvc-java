package spring.web.kotlin.global.common

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getMemberId(): Long? {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return principal.toString().toLongOrNull()
    }
}