package spring.web.kotlin.global.exception

import org.springframework.http.HttpStatus

abstract class BaseException(
    message: String,
    val httpStatus: HttpStatus
) : RuntimeException(message)