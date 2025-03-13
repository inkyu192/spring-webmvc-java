package spring.web.kotlin.presentation.dto.request

import jakarta.validation.constraints.Min

data class OrderItemSaveRequest(
    val itemId: Long,
    @field:Min(1)
    val count: Int,
)
