package spring.web.kotlin.repository

import org.springframework.data.repository.CrudRepository
import spring.web.kotlin.domain.Token

interface TokenRepository : CrudRepository<Token, Long> {
}