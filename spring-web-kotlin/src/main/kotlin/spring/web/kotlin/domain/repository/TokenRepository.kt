package spring.web.kotlin.domain.repository

import spring.web.kotlin.domain.model.entity.Token

interface TokenRepository {
    fun findByIdOrNull(id: Long): Token?
    fun save(token: Token): Token
}