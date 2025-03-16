package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.repository.ItemRepository
import spring.web.kotlin.infrastructure.persistence.ItemJpaRepository
import spring.web.kotlin.infrastructure.persistence.ItemQuerydslRepository

@Component
class ItemRepositoryAdapter(
    private val jpaRepository: ItemJpaRepository,
    private val querydslRepository: ItemQuerydslRepository,
) : ItemRepository {

    override fun findAll(pageable: Pageable, name: String?) = querydslRepository.findAll(pageable, name)

    override fun findByIdOrNull(id: Long) = jpaRepository.findByIdOrNull(id)

    override fun save(item: Item) = jpaRepository.save(item)

    override fun saveAll(items: Iterable<Item>): List<Item> = jpaRepository.saveAll(items)

    override fun delete(item: Item) {
        jpaRepository.delete(item)
    }
}