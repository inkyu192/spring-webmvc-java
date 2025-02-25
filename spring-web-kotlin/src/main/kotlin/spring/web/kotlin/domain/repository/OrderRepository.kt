package spring.web.kotlin.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.enums.DeliveryStatus
import spring.web.kotlin.domain.model.enums.OrderStatus

interface OrderRepository {
    fun findAll(
        pageable: Pageable,
        memberId: Long?,
        orderStatus: OrderStatus?,
        deliveryStatus: DeliveryStatus?
    ): Page<Order>

    fun findByIdOrNull(id: Long): Order?
    fun save(order: Order): Order
}