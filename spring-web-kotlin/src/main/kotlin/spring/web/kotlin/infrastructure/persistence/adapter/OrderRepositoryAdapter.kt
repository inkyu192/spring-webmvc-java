package spring.web.kotlin.infrastructure.persistence.adapter

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.enums.OrderStatus
import spring.web.kotlin.domain.repository.OrderRepository
import spring.web.kotlin.infrastructure.persistence.OrderJpaRepository
import spring.web.kotlin.infrastructure.persistence.OrderQuerydslRepository

@Component
class OrderRepositoryAdapter(
    private val jpaRepository: OrderJpaRepository,
    private val querydslRepository: OrderQuerydslRepository,
) : OrderRepository {
    override fun findAll(pageable: Pageable, memberId: Long?, orderStatus: OrderStatus?) =
        querydslRepository.findAll(pageable, memberId, orderStatus)

    override fun findByIdOrNull(id: Long) = jpaRepository.findByIdOrNull(id)

    override fun save(order: Order) = jpaRepository.save(order)
}