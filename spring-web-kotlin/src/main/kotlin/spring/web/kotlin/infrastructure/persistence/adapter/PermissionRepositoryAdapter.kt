package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.repository.PermissionRepository
import spring.web.kotlin.infrastructure.persistence.PermissionJpaRepository

@Repository
class PermissionRepositoryAdapter(
    private val jpaRepository: PermissionJpaRepository
) : PermissionRepository {
    override fun findByIdOrNull(id: Long) = jpaRepository.findByIdOrNull(id)
}