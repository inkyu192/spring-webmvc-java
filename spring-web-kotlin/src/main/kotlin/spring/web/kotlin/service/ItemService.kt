package spring.web.kotlin.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.Item
import spring.web.kotlin.dto.response.ItemResponse
import spring.web.kotlin.dto.request.ItemSaveRequest
import spring.web.kotlin.repository.ItemRepository

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun saveItem(itemSaveRequest: ItemSaveRequest): ItemResponse {
        val item = Item.create(
            itemSaveRequest.name,
            itemSaveRequest.description,
            itemSaveRequest.price,
            itemSaveRequest.quantity,
            itemSaveRequest.category
        )

        itemRepository.save(item)

        return ItemResponse(item)
    }

    fun findItems(pageable: Pageable, name: String): Page<ItemResponse> {
        return itemRepository.findAllWithJpql(pageable, name)
            .map { item -> ItemResponse(item) }
    }
}