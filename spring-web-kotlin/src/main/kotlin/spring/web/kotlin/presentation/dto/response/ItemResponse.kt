package spring.web.kotlin.presentation.dto.response

import spring.web.kotlin.domain.model.entity.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int
) {
    constructor(item: Item) : this(
        id = item.id!!,
        name = item.name,
        description = item.description,
        price = item.price,
        quantity = item.quantity
    )
}
