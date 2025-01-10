package spring.web.kotlin.domain.item

import jakarta.persistence.*
import org.springframework.http.HttpStatus
import spring.web.kotlin.domain.Base
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.exception.DomainException

@Entity
class Item protected constructor(
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null,
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
            throw DomainException(ResponseMessage.INSUFFICIENT_QUANTITY, HttpStatus.CONFLICT)
        }

        this.quantity = differenceQuantity
    }

    fun addQuantity(quantity: Int) {
        this.quantity += quantity
    }

    enum class Category(
        val description: String
    ) {
        ROLE_BOOK("책"),
        ROLE_TICKET("표")
    }
}
