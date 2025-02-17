package spring.web.kotlin.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponse
import spring.web.kotlin.global.common.ResponseMessage

class BaseException(
    val responseMessage: ResponseMessage,
    val httpStatus: HttpStatus
) : RuntimeException(), ErrorResponse {
    override fun getStatusCode() = httpStatus

    override fun getBody() = ProblemDetail.forStatus(httpStatus).apply {
        title = httpStatus.reasonPhrase
        detail = responseMessage.detail
    }
}