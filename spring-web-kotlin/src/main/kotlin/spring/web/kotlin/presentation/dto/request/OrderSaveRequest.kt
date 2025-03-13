package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Size

data class OrderSaveRequest(
    val memberId: Long,
    val city: String,
    val street: String,
    val zipcode: String,
    @field:Size(min = 1)
    val orderItems: List<OrderItemSaveRequest> = emptyList(),
)
