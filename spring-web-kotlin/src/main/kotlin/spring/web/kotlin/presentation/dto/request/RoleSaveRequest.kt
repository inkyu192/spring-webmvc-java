package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Size

data class RoleSaveRequest(
    val name: String,
    @Size(min = 1)
    val permissionIds: List<Long> = emptyList(),
)
