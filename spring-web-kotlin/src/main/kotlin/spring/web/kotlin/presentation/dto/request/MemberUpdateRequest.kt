package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Pattern
import java.time.LocalDate


data class MemberUpdateRequest(
    val password: String?,
    val name: String?,
    @field:Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
    val phone: String?,
    val birthDate: LocalDate?,
)
