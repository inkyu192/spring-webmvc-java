package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Permission
import spring.web.kotlin.domain.model.entity.Role

data class RoleResponse(
    val id: Long,
    val name: String,
    val permissions: List<PermissionResponse>
) {
    constructor(role: Role, permissions: List<Permission>) : this(
        id = role.id!!,
        name = role.name,
        permissions = permissions.map { PermissionResponse(it) }
    )
}