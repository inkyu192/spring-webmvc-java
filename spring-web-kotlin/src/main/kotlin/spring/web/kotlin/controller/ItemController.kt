package spring.web.kotlin.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.web.kotlin.dto.response.ItemResponse
import spring.web.kotlin.dto.request.ItemSaveRequest
import spring.web.kotlin.dto.response.ApiResponse
import spring.web.kotlin.service.ItemService

@RestController
@RequestMapping("item")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    fun saveItem(@RequestBody itemSaveRequest: ItemSaveRequest): ApiResponse<ItemResponse> {
        val saveItem = itemService.saveItem(itemSaveRequest)

        return ApiResponse(saveItem)
    }

    @GetMapping
    fun findItems(pageable: Pageable, @RequestParam(required = false) name: String): ApiResponse<Page<ItemResponse>> {
        val page = itemService.findItems(pageable, name)

        return ApiResponse(page)
    }
}