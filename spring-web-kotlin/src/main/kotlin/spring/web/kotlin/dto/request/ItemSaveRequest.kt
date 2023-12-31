package spring.web.kotlin.dto.request

import spring.web.kotlin.constant.Category

data class ItemSaveRequest(
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int,
    val category: Category
)
