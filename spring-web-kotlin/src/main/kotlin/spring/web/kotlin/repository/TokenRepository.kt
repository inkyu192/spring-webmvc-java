package spring.web.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.Token

interface TokenRepository : JpaRepository<Token, Long> {
}