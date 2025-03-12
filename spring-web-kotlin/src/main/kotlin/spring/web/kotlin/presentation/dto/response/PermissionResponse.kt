package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Permission

data class PermissionResponse(
    val id: Long,
    val name: String
) {
    constructor(permission: Permission) : this(
        id = requireNotNull(permission.id),
        name = permission.name
    )
}