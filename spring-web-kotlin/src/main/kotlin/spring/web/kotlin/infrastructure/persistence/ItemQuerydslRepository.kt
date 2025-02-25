package spring.web.kotlin.infrastructure.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.entity.Item
import spring.web.kotlin.domain.model.entity.QItem.item

@Component
class ItemQuerydslRepository(
    entityManager: EntityManager
) {
    private val queryFactory = JPAQueryFactory(entityManager)

    fun findAll(pageable: Pageable, name: String?): Page<Item> {
        val count = queryFactory
            .select(item.count())
            .from(item)
            .where(likeName(name))
            .fetchOne() ?: 0

        val content = queryFactory
            .selectFrom(item)
            .where(likeName(name))
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        return PageImpl(content, pageable, count)
    }

    private fun likeName(name: String?) = name.takeIf { !it.isNullOrBlank() }?.let { item.name.like("%$it%") }
}