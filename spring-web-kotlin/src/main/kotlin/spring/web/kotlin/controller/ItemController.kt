package spring.web.kotlin.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.dto.request.ItemSaveRequest
import spring.web.kotlin.dto.response.ApiResponse
import spring.web.kotlin.service.ItemService

@RestController
@RequestMapping("item")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    fun saveItem(@RequestBody itemSaveRequest: ItemSaveRequest) = ApiResponse(itemService.saveItem(itemSaveRequest))

    @GetMapping
    fun findItems(pageable: Pageable, @RequestParam(required = false) name: String?) =
        ApiResponse(itemService.findItems(pageable, name))
}