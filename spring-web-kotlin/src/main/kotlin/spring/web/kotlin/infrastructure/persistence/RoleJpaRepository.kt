package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.model.entity.Role

interface RoleJpaRepository: JpaRepository<Role, Long> {
}