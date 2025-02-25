package spring.web.kotlin.domain.repository

interface RequestLockRepository {
    fun setIfAbsent(memberId: Long, method: String, uri: String): Boolean
}