package spring.web.kotlin.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.web.kotlin.constant.ApiResponseCode
import spring.web.kotlin.domain.Item
import spring.web.kotlin.dto.request.ItemSaveRequest
import spring.web.kotlin.dto.response.ItemResponse
import spring.web.kotlin.exception.CommonException
import spring.web.kotlin.repository.ItemRepository

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun saveItem(itemSaveRequest: ItemSaveRequest) = Item.create(
        itemSaveRequest.name,
        itemSaveRequest.description,
        itemSaveRequest.price,
        itemSaveRequest.quantity,
        itemSaveRequest.category
    )
        .also { itemRepository.save(it) }
        .let { ItemResponse(it) }

    fun findItems(pageable: Pageable, name: String?) = itemRepository.findAllWithJpql(pageable, name)
        .map { ItemResponse(it) }

    fun findItem(id: Long) = itemRepository.findById(id).orElse(null)
        ?.let { ItemResponse(it) }
        ?: CommonException(ApiResponseCode.DATA_NOT_FOUND)

    @Transactional
    fun updateItem(id: Long, itemSaveRequest: ItemSaveRequest) = itemRepository.findById(id).orElse(null)
        ?.also {
            it.update(
                itemSaveRequest.name,
                itemSaveRequest.description,
                itemSaveRequest.price,
                itemSaveRequest.quantity,
                itemSaveRequest.category
            )
        }
        ?: Item.create(
            itemSaveRequest.name,
            itemSaveRequest.description,
            itemSaveRequest.price,
            itemSaveRequest.quantity,
            itemSaveRequest.category
        )
            .let { ItemResponse(it) }


    @Transactional
    fun deleteItem(id: Long) = itemRepository.deleteById(id)
}