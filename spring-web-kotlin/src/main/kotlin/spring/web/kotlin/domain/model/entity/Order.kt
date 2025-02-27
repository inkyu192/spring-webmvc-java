package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import org.springframework.http.HttpStatus
import spring.web.kotlin.domain.model.enums.DeliveryStatus
import spring.web.kotlin.domain.model.enums.OrderStatus
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    val id: Long? = null,

    val orderDate: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "delivery_id")
    var delivery: Delivery? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    private val _orderItems: MutableList<OrderItem> = mutableListOf(),
) : Base() {
    @get:Transient
    val orderItems: List<OrderItem>
        get() = _orderItems.toList()

    companion object {
        fun create(member: Member, delivery: Delivery, orderItems: List<OrderItem>) =
            Order(
                member = member,
                status = OrderStatus.ORDER,
                orderDate = LocalDateTime.now()
            ).apply {
                associateDelivery(delivery)
                orderItems.forEach { associateItem(it) }
            }
    }

    fun associateDelivery(delivery: Delivery) {
        this.delivery = delivery
        delivery.order = this
    }

    fun associateItem(orderItem: OrderItem) {
        _orderItems.add(orderItem)
        orderItem.order = this
    }

    fun cancel() {
        if (delivery?.status == DeliveryStatus.COMP) {
            throw BaseException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED, HttpStatus.UNPROCESSABLE_ENTITY)
        }

        status = OrderStatus.CANCEL
        orderItems.forEach { it.cancel() }
        delivery?.cancel()
    }
}
