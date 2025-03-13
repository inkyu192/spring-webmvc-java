package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.enums.OrderStatus
import java.time.Instant

data class OrderResponse(
    val id: Long,
    val name: String,
    val orderedAt: Instant,
    val status: OrderStatus,
    val orderItems: List<OrderItemResponse>
) {
    constructor(order: Order) : this(
        id = checkNotNull(order.id),
        name = order.member.name,
        orderedAt = order.orderedAt,
        status = order.status,
        orderItems = order.orderItems.map { OrderItemResponse(it) }
    )
}