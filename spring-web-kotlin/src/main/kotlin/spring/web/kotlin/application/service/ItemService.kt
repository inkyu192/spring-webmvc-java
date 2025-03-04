package spring.web.kotlin.application.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.presentation.dto.request.ItemSaveRequest
import spring.web.kotlin.presentation.dto.response.ItemResponse
import spring.web.kotlin.presentation.exception.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository,
) {
    @Transactional
    fun saveItem(itemSaveRequest: ItemSaveRequest): ItemResponse {
        val item = Item.create(
            itemSaveRequest.name,
            itemSaveRequest.description,
            itemSaveRequest.price,
            itemSaveRequest.quantity,
            itemSaveRequest.category,
        )

        itemRepository.save(item)

        return ItemResponse(item)
    }

    fun findItems(pageable: Pageable, name: String?) =
        itemRepository.findAll(pageable, name).map { ItemResponse(it) }

    fun findItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw EntityNotFoundException(Item::class.java, id)

        return ItemResponse(item)
    }

    @Transactional
    fun putItem(id: Long, itemSaveRequest: ItemSaveRequest) =
        itemRepository.findByIdOrNull(id)
            ?.let { false to updateItem(it, itemSaveRequest) }
            ?: (true to saveItem(itemSaveRequest))

    private fun updateItem(item: Item, itemSaveRequest: ItemSaveRequest): ItemResponse {
        item.update(
            itemSaveRequest.name,
            itemSaveRequest.description,
            itemSaveRequest.price,
            itemSaveRequest.quantity,
            itemSaveRequest.category,
        )

        return ItemResponse(item)
    }

    @Transactional
    fun deleteItem(id: Long) {
        val item = itemRepository.findByIdOrNull(id) ?: throw EntityNotFoundException(Item::class.java, id)

        itemRepository.delete(item)
    }
}
