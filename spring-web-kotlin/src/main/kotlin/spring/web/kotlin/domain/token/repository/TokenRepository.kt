package spring.web.kotlin.domain.token.repository

import org.springframework.data.repository.CrudRepository
import spring.web.kotlin.domain.token.Token

interface TokenRepository : CrudRepository<Token, Long> {
}