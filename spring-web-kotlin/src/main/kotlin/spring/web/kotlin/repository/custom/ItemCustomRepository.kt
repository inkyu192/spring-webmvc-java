package spring.web.kotlin.repository.custom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.Item

interface ItemCustomRepository {
    fun findAllWithJpql(pageable: Pageable, name: String): Page<Item>
}