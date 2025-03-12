package spring.web.kotlin.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Permission
import spring.web.kotlin.domain.model.entity.Role
import spring.web.kotlin.domain.model.entity.RolePermission
import spring.web.kotlin.domain.repository.PermissionRepository
import spring.web.kotlin.domain.repository.RoleRepository
import spring.web.kotlin.presentation.dto.request.RoleSaveRequest
import spring.web.kotlin.presentation.dto.response.RoleResponse
import spring.web.kotlin.presentation.exception.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class RoleService(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {
    @Transactional
    fun saveRole(roleSaveRequest: RoleSaveRequest): RoleResponse {
        val rolePermissions = roleSaveRequest.permissionIds.map {
            val permission = permissionRepository.findByIdOrNull(it)
                ?: throw EntityNotFoundException(Permission::class.java, it)

            RolePermission.create(permission)
        }

        val role = roleRepository.save(Role.create(roleSaveRequest.name, rolePermissions))

        return RoleResponse(role, role.rolePermissions.mapNotNull { it.permission })
    }
}