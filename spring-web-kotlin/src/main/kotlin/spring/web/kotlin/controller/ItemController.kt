package spring.web.kotlin.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.web.kotlin.dto.response.ItemResponse
import spring.web.kotlin.dto.request.ItemSaveRequest
import spring.web.kotlin.dto.response.ApiResponse
import spring.web.kotlin.service.ItemService

@RestController
@RequestMapping("item")
class ItemController(
    val itemService: ItemService
) {

    @PostMapping
    fun saveItem(itemSaveRequest: ItemSaveRequest): ApiResponse<ItemResponse> {
        val saveItem = itemService.saveItem(itemSaveRequest)

        return ApiResponse(saveItem)
    }
}