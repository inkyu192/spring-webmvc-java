package spring.web.kotlin.domain.repository

import spring.web.kotlin.domain.model.entity.Role

interface RoleRepository {
    fun save(role: Role): Role
    fun findByIdOrNull(id: Long): Role?
}