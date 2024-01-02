package spring.web.kotlin.repository.custom

import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spring.web.kotlin.domain.Item

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

        name?.let {
            countJpql += " AND i.name LIKE CONCAT('%', :name, '%')"
            contentJpql += " AND i.name LIKE CONCAT('%', :name, '%')"
        }

        pageable.sort
            .takeIf { it.isSorted }
            ?.run {
                val orderList = map { order -> " i.${order.property} ${order.direction}" }
                contentJpql += " ORDER BY " + orderList.joinToString(",")
            }

        val countQuery: TypedQuery<Long> = entityManager.createQuery(countJpql, Long::class.java)
        val contentQuery: TypedQuery<Item> = entityManager.createQuery(contentJpql, Item::class.java)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)

        name?.let {
            countQuery.setParameter("name", name)
            contentQuery.setParameter("name", name)
        }

        return PageImpl(contentQuery.resultList, pageable, countQuery.singleResult)
    }
}