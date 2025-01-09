package spring.web.kotlin.domain.item.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import spring.web.kotlin.domain.item.Item

class ItemCustomRepositoryImpl(
    private val entityManager: EntityManager
) : ItemCustomRepository {
    override fun findAllWithJpql(pageable: Pageable, name: String?): Page<Item> {
        var countJpql: String = """
            SELECT COUNT(i)
            FROM Item i
            WHERE 1 = 1
        """.trimIndent()

        var contentJpql: String = """
            SELECT i
            FROM Item i
            WHERE 1 = 1
        """.trimIndent()

        if (StringUtils.hasText(name)) {
            countJpql += " AND i.name LIKE CONCAT('%', :name, '%')"
            contentJpql += " AND i.name LIKE CONCAT('%', :name, '%')"
        }

        val sort = pageable.sort
        if (sort.isSorted) {
            val orderList = sort
                .map { order -> " i.${order.property} ${order.direction}" }
                .toList()

            contentJpql += " ORDER BY " + orderList.joinToString(",")
        }

        val countQuery: TypedQuery<Long> = entityManager.createQuery(countJpql, Long::class.java)
        val contentQuery: TypedQuery<Item> = entityManager.createQuery(contentJpql, Item::class.java)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)

        if (StringUtils.hasText(name)) {
            countQuery.setParameter("name", name)
            contentQuery.setParameter("name", name)
        }

        return PageImpl(contentQuery.resultList, pageable, countQuery.singleResult)
    }
}