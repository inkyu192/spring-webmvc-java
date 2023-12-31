package spring.web.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.Item

@Repository
interface ItemRepository: JpaRepository<Item, Long> {
}