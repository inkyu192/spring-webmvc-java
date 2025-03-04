package spring.web.kotlin.infrastructure.util

import org.springframework.security.core.context.SecurityContextHolder

object SecurityContextUtil {
    fun getMemberId() = SecurityContextHolder.getContext().authentication?.principal.toString().toLong()
}