package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "order_item")
class OrderItem protected constructor(
    val orderPrice: Int,
    val count: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var item: Item,
) {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var order: Order? = null
        protected set

    companion object {
        fun create(item: Item, count: Int) =
            OrderItem(
                orderPrice = item.price,
                count = count,
                item = item
            ).apply { item.removeQuantity(count) }
    }

    fun associateOrder(order: Order) {
        this.order = order
    }

    fun cancel() {
        item.addQuantity(count)
    }
}