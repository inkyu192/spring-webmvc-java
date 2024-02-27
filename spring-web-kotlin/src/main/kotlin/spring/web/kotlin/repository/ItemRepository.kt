package spring.web.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.Item
import spring.web.kotlin.repository.custom.ItemCustomRepository

interface ItemRepository : JpaRepository<Item, Long>, ItemCustomRepository