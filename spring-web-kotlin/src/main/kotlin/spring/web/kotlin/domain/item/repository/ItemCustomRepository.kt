package spring.web.kotlin.domain.item.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.item.Item

interface ItemCustomRepository {
    fun findAllWithJpql(pageable: Pageable, name: String?): Page<Item>
}