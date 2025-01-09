package spring.web.kotlin.domain.item.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.domain.item.dto.ItemSaveRequest
import spring.web.kotlin.global.common.ApiResponse
import spring.web.kotlin.domain.item.service.ItemService

@RestController
@RequestMapping("item")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    fun saveItem(@RequestBody itemSaveRequest: ItemSaveRequest) =
        ApiResponse(itemService.saveItem(itemSaveRequest))

    @GetMapping
    fun findItems(pageable: Pageable, @RequestParam(required = false) name: String?) =
        ApiResponse(itemService.findItems(pageable, name))

    @GetMapping("{id}")
    fun findItem(@PathVariable id: Long) = ApiResponse(itemService.findItem(id))

    @PutMapping("{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody itemSaveRequest: ItemSaveRequest) =
        ApiResponse(itemService.updateItem(id, itemSaveRequest))

    @DeleteMapping("{id}")
    fun deleteItem(@PathVariable id: Long): ApiResponse<Unit> {
        itemService.deleteItem(id)

        return ApiResponse()
    }
}