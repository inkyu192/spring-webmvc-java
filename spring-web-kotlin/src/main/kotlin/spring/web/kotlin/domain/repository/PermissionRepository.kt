package spring.web.kotlin.domain.repository

import spring.web.kotlin.domain.model.entity.Permission

interface PermissionRepository {
    fun findByIdOrNull(id: Long): Permission?
}