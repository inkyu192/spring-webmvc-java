package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.repository.CrudRepository
import spring.web.kotlin.domain.model.entity.Token

interface TokenRedisRepository : CrudRepository<Token, Long> {
}