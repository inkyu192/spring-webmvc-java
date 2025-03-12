package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import spring.web.kotlin.domain.model.enums.Category
import spring.web.kotlin.presentation.exception.InsufficientQuantityException

@Entity
class Item protected constructor(
    name: String,
    description: String,
    price: Int,
    quantity: Int,
    category: Category,
) : Base() {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null
        protected set

    var name: String = name
        protected set

    var description: String = description
        protected set

    var price: Int = price
        protected set

    var quantity: Int = quantity
        protected set

    @Enumerated(EnumType.STRING)
    var category: Category = category
        protected set

    companion object {
        fun create(name: String, description: String, price: Int, quantity: Int, category: Category) =
            Item(
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                category = category,
            )
    }

    fun update(name: String, description: String, price: Int, quantity: Int, category: Category) {
        this.name = name
        this.description = description
        this.price = price
        this.quantity = quantity
        this.category = category
    }

    fun removeQuantity(quantity: Int) {

        val differenceQuantity = this.quantity - quantity

        if (differenceQuantity < 0) {
            throw InsufficientQuantityException(name, quantity, this.quantity)
        }

        this.quantity = differenceQuantity
    }

    fun addQuantity(quantity: Int) {
        this.quantity += quantity
    }
}
