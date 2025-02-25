package spring.web.kotlin.presentation.dto.request

data class RoleSaveRequest(
    val name: String,
    val permissionIds: List<Long>
)
