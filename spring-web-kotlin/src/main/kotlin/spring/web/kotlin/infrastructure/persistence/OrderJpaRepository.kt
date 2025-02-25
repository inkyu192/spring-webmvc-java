package spring.web.kotlin.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import spring.web.kotlin.domain.model.entity.Order

interface OrderJpaRepository: JpaRepository<Order, Long> {
}