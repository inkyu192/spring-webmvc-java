package spring.web.kotlin.domain

import jakarta.persistence.*
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.constant.Category
import spring.web.kotlin.exception.CommonException

@Entity
class Item private constructor(
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    val id: Long? = null,
    var name: String,
    var description: String,
    var price: Int,
    var quantity: Int,

    @Enumerated(EnumType.STRING)
    var category: Category
) : Base() {
    companion object {
        fun create(name: String, description: String, price: Int, quantity: Int, category: Category) =
            Item(
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                category = category
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
            throw CommonException(ApiResponseCode.QUANTITY_NOT_ENOUGH)
        }

        this.quantity = differenceQuantity
    }

    fun addQuantity(quantity: Int) {
        this.quantity += quantity
    }
}