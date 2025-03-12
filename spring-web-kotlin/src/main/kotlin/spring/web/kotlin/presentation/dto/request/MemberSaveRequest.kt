package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.time.LocalDate


data class MemberSaveRequest(
    @field:Email
    val account: String,
    val password: String,
    val name: String,
    @field:Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
    val phone: String,
    val birthdate: LocalDate,
    val roleIds: List<Long> = emptyList(),
    val permissionIds: List<Long> = emptyList(),
)
