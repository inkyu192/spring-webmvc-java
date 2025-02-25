package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.model.entity.Role
import spring.web.kotlin.domain.repository.RoleRepository
import spring.web.kotlin.infrastructure.persistence.RoleJpaRepository

@Repository
class RoleRepositoryAdapter(
    private val jpaRepository: RoleJpaRepository
) : RoleRepository {
    override fun save(role: Role) = jpaRepository.save(role)

    override fun findByIdOrNull(id: Long) = jpaRepository.findByIdOrNull(id)
}