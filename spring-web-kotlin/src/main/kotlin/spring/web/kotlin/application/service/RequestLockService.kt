package spring.web.kotlin.application.service

import org.springframework.stereotype.Service
import spring.web.kotlin.domain.repository.RequestLockRepository
import spring.web.kotlin.presentation.exception.DuplicateRequestException

@Service
class RequestLockService(
    private val requestLockRepository: RequestLockRepository
) {
    fun validate(memberId: Long, method: String, uri: String) {
        if (!requestLockRepository.setIfAbsent(memberId, method, uri)) {
            throw DuplicateRequestException(memberId, method, uri)
        }
    }
}