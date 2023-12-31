package spring.web.kotlin.dto.response

import spring.web.kotlin.domain.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int
) {
    constructor(item: Item) : this(
        item.id!!,
        item.name,
        item.description,
        item.price,
        item.quantity
    )
}
