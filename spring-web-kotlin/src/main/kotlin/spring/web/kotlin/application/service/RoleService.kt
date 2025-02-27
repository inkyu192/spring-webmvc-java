package spring.web.kotlin.application.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Role
import spring.web.kotlin.domain.model.entity.RolePermission
import spring.web.kotlin.domain.repository.PermissionRepository
import spring.web.kotlin.domain.repository.RoleRepository
import spring.web.kotlin.presentation.dto.request.RoleSaveRequest
import spring.web.kotlin.presentation.dto.response.RoleResponse
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode

@Service
@Transactional(readOnly = true)
class RoleService(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {
    @Transactional
    fun saveRole(roleSaveRequest: RoleSaveRequest): RoleResponse {
        val role = Role.create(roleSaveRequest.name)

        roleSaveRequest.permissionIds.forEach {
            val permission = permissionRepository.findByIdOrNull(it)
                ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

            role.associatePermission(RolePermission.create(permission))
        }

        roleRepository.save(role)

        return RoleResponse(role, role.rolePermissions.map { it.permission })
    }
}