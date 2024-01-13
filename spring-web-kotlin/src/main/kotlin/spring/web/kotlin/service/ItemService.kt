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
    fun saveItem(itemSaveRequest: ItemSaveRequest) = ItemResponse(
        itemRepository.save(
            Item.create(
                itemSaveRequest.name,
                itemSaveRequest.description,
                itemSaveRequest.price,
                itemSaveRequest.quantity,
                itemSaveRequest.category
            )
        )
    )

    fun findItems(pageable: Pageable, name: String?) = itemRepository.findAllWithJpql(pageable, name)
        .map { item -> ItemResponse(item) }

    fun findItem(id: Long) = itemRepository.findById(id)
        .map { ItemResponse(it) }
        .orElseThrow { CommonException(ApiResponseCode.DATA_NOT_FOUND) }

    @Transactional
    fun updateItem(id: Long, itemSaveRequest: ItemSaveRequest) = ItemResponse(
        itemRepository.findById(id)
            .map {
                it.update(
                    itemSaveRequest.name,
                    itemSaveRequest.description,
                    itemSaveRequest.price,
                    itemSaveRequest.quantity,
                    itemSaveRequest.category
                )
                it
            }
            .orElseGet {
                Item.create(
                    itemSaveRequest.name,
                    itemSaveRequest.description,
                    itemSaveRequest.price,
                    itemSaveRequest.quantity,
                    itemSaveRequest.category
                )
            }
    )
}