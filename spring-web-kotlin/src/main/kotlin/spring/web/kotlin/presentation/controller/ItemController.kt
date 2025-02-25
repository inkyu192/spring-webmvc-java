package spring.web.kotlin.presentation.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.presentation.dto.request.ItemSaveRequest
import spring.web.kotlin.application.service.ItemService

@RestController
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService
) {
    @PostMapping
    @PreAuthorize("hasAuthority('ITEM_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveItem(@RequestBody @Validated itemSaveRequest: ItemSaveRequest) = itemService.saveItem(itemSaveRequest)

    @GetMapping
    @PreAuthorize("hasAuthority('ITEM_READ')")
    fun findItems(@PageableDefault pageable: Pageable, @RequestParam(required = false) name: String?) =
        itemService.findItems(pageable, name)

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_READ')")
    fun findItem(@PathVariable id: Long) = itemService.findItem(id)

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_UPDATE')")
    fun putItem(@PathVariable id: Long, @RequestBody @Validated itemSaveRequest: ItemSaveRequest) =
        itemService.putItem(id, itemSaveRequest)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItem(@PathVariable id: Long) {
        itemService.deleteItem(id)
    }
}