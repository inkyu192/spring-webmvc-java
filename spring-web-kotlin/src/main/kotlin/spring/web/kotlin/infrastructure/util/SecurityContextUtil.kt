package spring.web.kotlin.infrastructure.util

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import spring.web.kotlin.presentation.exception.ErrorCode
import spring.web.kotlin.presentation.exception.BaseException

object SecurityContextUtil {
    fun getMemberId() = SecurityContextHolder.getContext().authentication?.principal.toString().toLongOrNull()
        ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
}