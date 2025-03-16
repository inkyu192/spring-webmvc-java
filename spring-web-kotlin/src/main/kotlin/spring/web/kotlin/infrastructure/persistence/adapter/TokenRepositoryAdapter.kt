package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.entity.Token
import spring.web.kotlin.domain.repository.TokenRepository
import spring.web.kotlin.infrastructure.persistence.TokenRedisRepository

@Component
class TokenRepositoryAdapter(
    private val redisRepository: TokenRedisRepository
) : TokenRepository {
    override fun findByIdOrNull(id: Long) = redisRepository.findByIdOrNull(id)

    override fun save(token: Token) = redisRepository.save(token)
}