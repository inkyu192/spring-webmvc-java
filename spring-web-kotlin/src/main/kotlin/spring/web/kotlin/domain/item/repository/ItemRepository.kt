package spring.web.kotlin.domain.item.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.item.Item

interface ItemRepository : JpaRepository<Item, Long>, ItemCustomRepository