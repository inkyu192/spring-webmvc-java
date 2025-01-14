package spring.web.kotlin.domain.item.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.domain.item.Item
import spring.web.kotlin.domain.item.dto.ItemResponse
import spring.web.kotlin.domain.item.dto.ItemSaveRequest
import spring.web.kotlin.domain.item.repository.ItemRepository
import spring.web.kotlin.global.common.ResponseMessage
import spring.web.kotlin.global.exception.DomainException

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository,
) {
    @Transactional
    fun saveItem(itemSaveRequest: ItemSaveRequest): ItemResponse {
        val item = itemRepository.save(
            Item.create(
                itemSaveRequest.name,
                itemSaveRequest.description,
                itemSaveRequest.price,
                itemSaveRequest.quantity,
                itemSaveRequest.category,
            ),
        )

        return ItemResponse(item)
    }

    fun findItems(pageable: Pageable, name: String?) = itemRepository
        .findAllWithJpql(pageable, name)
        .map { ItemResponse(it) }

    fun findItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id)
            ?: throw DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND)

        return ItemResponse(item)
    }

    @Transactional
    fun putItem(id: Long, itemSaveRequest: ItemSaveRequest): ItemResponse {
        val item = itemRepository.findByIdOrNull(id)?.apply {
            update(
                itemSaveRequest.name,
                itemSaveRequest.description,
                itemSaveRequest.price,
                itemSaveRequest.quantity,
                itemSaveRequest.category,
            )
        } ?: itemRepository.save(
            Item.create(
                itemSaveRequest.name,
                itemSaveRequest.description,
                itemSaveRequest.price,
                itemSaveRequest.quantity,
                itemSaveRequest.category,
            ),
        )

        return ItemResponse(item)
    }

    @Transactional
    fun deleteItem(id: Long) = itemRepository.deleteById(id)
}
