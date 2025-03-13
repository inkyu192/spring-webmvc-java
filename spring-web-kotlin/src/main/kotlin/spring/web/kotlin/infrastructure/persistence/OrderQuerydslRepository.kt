package spring.web.kotlin.infrastructure.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.entity.Order
import spring.web.kotlin.domain.model.entity.QMember.member
import spring.web.kotlin.domain.model.entity.QOrder.order
import spring.web.kotlin.domain.model.enums.OrderStatus

@Component
class OrderQuerydslRepository(
    entityManager: EntityManager
) {
    private val queryFactory = JPAQueryFactory(entityManager)

    fun findAll(pageable: Pageable, memberId: Long?, orderStatus: OrderStatus?): Page<Order> {
        val count = queryFactory
            .select(order.count())
            .from(order)
            .join(order.member, member)
            .where(eqMemberId(memberId), eqOrderStatus(orderStatus))
            .fetchOne() ?: 0L

        val content = queryFactory
            .selectFrom(order)
            .join(order.member, member).fetchJoin()
            .where(eqMemberId(memberId), eqOrderStatus(orderStatus))
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun eqMemberId(memberId: Long?) = memberId?.let { member.id.eq(it) }

    private fun eqOrderStatus(orderStatus: OrderStatus?) = orderStatus?.let { order.status.eq(it) }
}