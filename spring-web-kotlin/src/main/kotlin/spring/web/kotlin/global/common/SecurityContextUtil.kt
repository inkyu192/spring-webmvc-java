package spring.web.kotlin.global.common

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import spring.web.kotlin.global.exception.BaseException

object SecurityContextUtil {
    fun getMemberId() = SecurityContextHolder.getContext().authentication?.principal.toString().toLongOrNull()
        ?: throw BaseException(ResponseMessage.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
}