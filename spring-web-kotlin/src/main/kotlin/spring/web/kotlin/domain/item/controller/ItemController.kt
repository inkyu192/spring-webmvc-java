package spring.web.kotlin.domain.item.controller

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.domain.item.dto.ItemSaveRequest
import spring.web.kotlin.domain.item.service.ItemService

@RestController
@RequestMapping("item")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    fun saveItem(@RequestBody itemSaveRequest: ItemSaveRequest) = itemService.saveItem(itemSaveRequest)

    @GetMapping
    fun findItems(pageable: Pageable, @RequestParam(required = false) name: String?) =
        itemService.findItems(pageable, name)

    @GetMapping("{id}")
    fun findItem(@PathVariable id: Long) = itemService.findItem(id)

    @PutMapping("{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody itemSaveRequest: ItemSaveRequest) =
        itemService.putItem(id, itemSaveRequest)

    @DeleteMapping("{id}")
    fun deleteItem(@PathVariable id: Long) {
        itemService.deleteItem(id)
    }
}