package spring.web.kotlin.domain.model.entity

import jakarta.persistence.*
import org.springframework.http.HttpStatus
import spring.web.kotlin.domain.model.enums.Category
import spring.web.kotlin.presentation.exception.BaseException
import spring.web.kotlin.presentation.exception.ErrorCode

@Entity
class Item protected constructor(
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
            throw BaseException(ErrorCode.INSUFFICIENT_QUANTITY, HttpStatus.CONFLICT)
        }

        this.quantity = differenceQuantity
    }

    fun addQuantity(quantity: Int) {
        this.quantity += quantity
    }
}
