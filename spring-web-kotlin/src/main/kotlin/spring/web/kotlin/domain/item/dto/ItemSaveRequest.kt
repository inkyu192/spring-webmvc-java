package spring.web.kotlin.domain.item.dto

import spring.web.kotlin.domain.item.Item


data class ItemSaveRequest(
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int,
    val category: Item.Category
)
