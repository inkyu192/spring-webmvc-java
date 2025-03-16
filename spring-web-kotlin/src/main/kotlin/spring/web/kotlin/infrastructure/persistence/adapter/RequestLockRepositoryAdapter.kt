package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.stereotype.Component
import spring.web.kotlin.domain.repository.RequestLockRepository
import spring.web.kotlin.infrastructure.persistence.RequestLockRedisRepository

@Component
class RequestLockRepositoryAdapter(
    private val redisRepository: RequestLockRedisRepository
) : RequestLockRepository {
    override fun setIfAbsent(memberId: Long, method: String, uri: String): Boolean {
        return redisRepository.setIfAbsent(memberId, method, uri)
    }
}