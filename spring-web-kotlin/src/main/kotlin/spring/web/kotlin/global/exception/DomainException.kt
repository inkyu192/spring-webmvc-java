package spring.web.kotlin.global.exception

import org.springframework.http.HttpStatus
import spring.web.kotlin.global.common.ResponseMessage

class DomainException(
    val responseMessage: ResponseMessage,
    httpStatus: HttpStatus,
) : BaseException(responseMessage.detail, httpStatus)