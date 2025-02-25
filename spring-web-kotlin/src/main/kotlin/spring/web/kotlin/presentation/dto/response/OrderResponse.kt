package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Address
import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.enums.OrderStatus
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val name: String,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val address: Address,
    val orderItems: List<OrderItemResponse>
) {
    constructor(order: Order) : this(
        id = order.id!!,
        name = order.member.name,
        orderDate = order.orderDate,
        status = order.status,
        address = order.member.address,
        orderItems = order.orderItems.map { OrderItemResponse(it) }
    )
}