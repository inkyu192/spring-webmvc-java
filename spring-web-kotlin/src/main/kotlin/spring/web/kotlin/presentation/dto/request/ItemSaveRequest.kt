package spring.web.kotlin.presentation.dto.request

import spring.web.kotlin.domain.model.enums.Category


data class ItemSaveRequest(
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int,
    val category: Category
)
