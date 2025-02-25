package spring.web.kotlin.application.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Address
import spring.web.kotlin.domain.model.entity.Delivery
import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.entity.OrderItem
import spring.web.kotlin.domain.model.enums.DeliveryStatus
import spring.web.kotlin.domain.model.enums.OrderStatus
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.domain.repository.MemberRepository
import spring.web.kotlin.domain.repository.OrderRepository
import spring.web.kotlin.presentation.dto.request.OrderSaveRequest
import spring.web.kotlin.presentation.dto.response.OrderResponse
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode

@Service
@Transactional(readOnly = true)
class OrderService(
    private val memberRepository: MemberRepository,
    private val itemRepository: ItemRepository,
    private val orderRepository: OrderRepository
) {
    @Transactional
    fun saveOrder(orderSaveRequest: OrderSaveRequest): OrderResponse {
        val member = memberRepository.findByIdOrNull(orderSaveRequest.memberId)
            ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        val orderItems = orderSaveRequest.orderItems.map { orderItem ->
            val item = itemRepository.findByIdOrNull(orderItem.itemId)
                ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)
            OrderItem.create(item, item.price, orderItem.count)
        }

        val delivery = Delivery.create(
            Address.create(
                orderSaveRequest.city,
                orderSaveRequest.street,
                orderSaveRequest.zipcode
            )
        )

        val order = Order.create(member, delivery, orderItems)

        return OrderResponse(orderRepository.save(order))
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
        val order = (orderRepository.findByIdOrNull(id)
            ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND))

        return OrderResponse(order)
    }

    @Transactional
    fun cancelOrder(id: Long): OrderResponse {
        val order = orderRepository.findByIdOrNull(id)
            ?: throw BaseException(ErrorCode.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        order.cancel()

        return OrderResponse(order)
    }
}