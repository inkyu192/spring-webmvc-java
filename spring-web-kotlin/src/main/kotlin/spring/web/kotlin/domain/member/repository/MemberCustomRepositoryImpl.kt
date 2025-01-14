package spring.web.kotlin.domain.member.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import spring.web.kotlin.domain.member.Member

class MemberCustomRepositoryImpl(
    private val entityManager: EntityManager
) : MemberCustomRepository {
    override fun findAllWithJpql(pageable: Pageable, account: String?, name: String?): Page<Member> {
        var countJpql = """
           SELECT COUNT(m)
                FROM Member m
                WHERE 1 = 1 
        """.trimIndent()

        var contentJpql: String = """
            SELECT m
                FROM Member m
                WHERE 1 = 1
        """.trimIndent()

        if (StringUtils.hasText(account)) {
            countJpql += " AND m.account LIKE CONCAT('%', :account, '%')"
            contentJpql += " AND m.account LIKE CONCAT('%', :account, '%')"
        }

        if (StringUtils.hasText(name)) {
            countJpql += " AND m.name LIKE CONCAT('%', :name, '%')"
            contentJpql += " AND m.name LIKE CONCAT('%', :name, '%')"
        }

        val sort = pageable.sort
        if (sort.isSorted) {
            val orderList = sort
                .map { order -> " i.${order.property} ${order.direction}" }
                .toList()

            contentJpql += " ORDER BY " + orderList.joinToString(",")
        }

        val countQuery: TypedQuery<Long> = entityManager.createQuery(countJpql, Long::class.java)
        val contentQuery: TypedQuery<Member> = entityManager.createQuery(contentJpql, Member::class.java)

        if (pageable.isPaged) {
            contentQuery.setFirstResult(pageable.offset.toInt()).setMaxResults(pageable.pageSize)
        }

        if (StringUtils.hasText(account)) {
            countQuery.setParameter("account", account)
            contentQuery.setParameter("account", account)
        }

        if (StringUtils.hasText(name)) {
            countQuery.setParameter("name", name)
            contentQuery.setParameter("name", name)
        }

        return PageImpl(contentQuery.resultList, pageable, countQuery.singleResult)
    }
}