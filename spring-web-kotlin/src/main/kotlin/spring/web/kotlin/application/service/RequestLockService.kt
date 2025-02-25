package spring.web.kotlin.application.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.web.kotlin.domain.repository.RequestLockRepository
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode

@Service
class RequestLockService(
    private val requestLockRepository: RequestLockRepository
) {
    fun validate(memberId: Long, method: String, uri: String) {
        if (!requestLockRepository.setIfAbsent(memberId, method, uri)) {
            throw BaseException(ErrorCode.DUPLICATE_REQUEST, HttpStatus.TOO_MANY_REQUESTS)
        }
    }
}