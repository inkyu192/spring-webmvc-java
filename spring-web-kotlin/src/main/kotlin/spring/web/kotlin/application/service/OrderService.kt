package spring.web.kotlin.application.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.*
import spring.web.kotlin.domain.model.enums.DeliveryStatus
import spring.web.kotlin.domain.model.enums.OrderStatus
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.OrderRepository
import spring.web.kotlin.presentation.dto.request.OrderSaveRequest
import spring.web.kotlin.presentation.dto.response.OrderResponse
import spring.web.kotlin.presentation.exception.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class OrderService(
    private val memberRepository: MemberRepository,
    private val itemRepository: ItemRepository,
    private val orderRepository: OrderRepository
) {
    @Transactional
    fun saveOrder(orderSaveRequest: OrderSaveRequest): OrderResponse {
        val (memberId, city, street, zipcode, requestOrderItems) = orderSaveRequest

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw EntityNotFoundException(Member::class.java, memberId)

        val orderItems = requestOrderItems.map { orderItem ->
            val item = itemRepository.findByIdOrNull(orderItem.itemId)
                ?: throw EntityNotFoundException(OrderItem::class.java, orderItem.itemId)

            OrderItem.create(item, item.price, orderItem.count)
        }

        val delivery = Delivery.create(Address.create(city, street, zipcode))
        val order = orderRepository.save(Order.create(member, delivery, orderItems))

        return OrderResponse(order)
    }

    fun findOrders(
        memberId: Long?,
        orderStatus: OrderStatus?,
        deliveryStatus: DeliveryStatus?,
        pageable: Pageable
    ): Page<OrderResponse> {
        return orderRepository.findAll(pageable, memberId, orderStatus, deliveryStatus)
            .map(::OrderResponse)
    }

    fun findOrder(id: Long): OrderResponse {
        val order = orderRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(Order::class.java, id)

        return OrderResponse(order)
    }

    @Transactional
    fun cancelOrder(id: Long): OrderResponse {
        val order = orderRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException(Order::class.java, id)

        order.cancel()

        return OrderResponse(order)
    }
}