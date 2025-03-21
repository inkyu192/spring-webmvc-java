package spring.web.kotlin.infrastructure.persistence

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.model.entity.QItem.item
import spring.web.kotlin.domain.model.entity.QOrderItem.orderItem

@Repository
class ItemQuerydslRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {
    fun findAll(pageable: Pageable, name: String?): Page<Item> {
        val count = jpaQueryFactory
            .select(item.count())
            .from(item)
            .where(likeName(name))
            .fetchOne() ?: 0

        val content = jpaQueryFactory
            .selectFrom(item)
            .where(likeName(name))
            .orderBy(createOrderSpecifier())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun likeName(name: String?) = name.takeIf { !it.isNullOrBlank() }?.let { item.name.like("%$it%") }

    private fun createOrderSpecifier() =
        OrderSpecifier(
            Order.DESC,
            JPAExpressions
                .select(orderItem.count)
                .from(orderItem)
                .where(orderItem.item.id.eq(item.id))
        )
}