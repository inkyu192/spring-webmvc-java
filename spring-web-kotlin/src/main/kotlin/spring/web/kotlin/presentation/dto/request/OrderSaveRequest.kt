package spring.web.kotlin.presentation.dto.request

data class OrderSaveRequest(
    val memberId: Long,
    val city: String,
    val street: String,
    val zipcode: String,
    val orderItems: List<OrderItemSaveRequest>
)
