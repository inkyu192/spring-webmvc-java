package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponse

class BaseException(
    val errorCode: ErrorCode,
    val httpStatus: HttpStatus
) : RuntimeException(), ErrorResponse {
    override fun getStatusCode() = httpStatus

    override fun getBody() = ProblemDetail.forStatus(httpStatus).apply {
        title = httpStatus.reasonPhrase
        detail = errorCode.detail
    }
}