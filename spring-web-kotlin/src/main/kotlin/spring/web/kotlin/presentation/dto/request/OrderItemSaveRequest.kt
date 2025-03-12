package spring.web.kotlin.presentation.dto.request

data class OrderItemSaveRequest(
    val itemId: Long,
    val count: Int,
)
