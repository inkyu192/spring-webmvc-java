package spring.web.kotlin.presentation.dto.request


data class MemberSaveRequest(
    val account: String,
    val password: String,
    val name: String,
    val city: String,
    val street: String,
    val zipcode: String,
    val roleIds: List<Long>?,
    val permissionIds: List<Long>?
)
