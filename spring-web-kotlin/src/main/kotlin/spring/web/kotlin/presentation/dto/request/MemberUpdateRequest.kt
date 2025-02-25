package spring.web.kotlin.presentation.dto.request


data class MemberUpdateRequest(
    val name: String,
    val city: String,
    val street: String,
    val zipcode: String
)
