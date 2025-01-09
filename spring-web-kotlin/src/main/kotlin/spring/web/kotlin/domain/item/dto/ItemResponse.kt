package spring.web.kotlin.domain.item.dto

import spring.web.kotlin.domain.item.Item

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
