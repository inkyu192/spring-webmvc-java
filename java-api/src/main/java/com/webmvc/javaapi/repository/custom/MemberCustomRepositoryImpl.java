package com.webmvc.javaapi.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webmvc.javaapi.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.webmvc.javaapi.domain.QMember.member;


public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findAllWithJpql(Pageable pageable, String account, String name) {
        String countJpql = """
                SELECT COUNT(m)
                FROM Member m
                WHERE 1 = 1
                """;

        String contentJpql = """
                SELECT m
                FROM Member m
                WHERE 1 = 1
                """;

        if (StringUtils.hasText(account)) {
            countJpql += " AND m.account LIKE CONCAT('%', :account, '%')";
            contentJpql += " AND m.account LIKE CONCAT('%', :account, '%')";
        }

        if (StringUtils.hasText(name)) {
            countJpql += " AND m.name LIKE CONCAT('%', :name, '%')";
            contentJpql += " AND m.name LIKE CONCAT('%', :name, '%')";
        }

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<String> orderList = sort
                    .map(order -> " m." + order.getProperty() + " " + order.getDirection())
                    .toList();

            contentJpql += " ORDER BY ";
            contentJpql += String.join(",", orderList);
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        TypedQuery<Member> contentQuery = entityManager.createQuery(contentJpql, Member.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        if (StringUtils.hasText(account)) {
            countQuery.setParameter("account", account);
            contentQuery.setParameter("account", account);
        }

        if (StringUtils.hasText(name)) {
            countQuery.setParameter("name", name);
            contentQuery.setParameter("name", name);
        }

        contentQuery.setFirstResult((int) pageable.getOffset());
        contentQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(contentQuery.getResultList(), pageable, countQuery.getSingleResult());
    }

    @Override
    public Page<Member> findAllWithQuerydsl(Pageable pageable, String account, String name) {
        int count = queryFactory
                .selectOne()
                .from(member)
                .where(
                        StringUtils.hasText(account) ? member.account.like("%" + account + "%") : null,
                        StringUtils.hasText(name) ? member.name.like("%" + name + "%") : null
                )
                .fetch()
                .size();

        List<Member> content = queryFactory
                .select(member)
                .from(member)
                .where(
                        StringUtils.hasText(account) ? member.account.like("%" + account + "%") : null,
                        StringUtils.hasText(name) ? member.name.like("%" + name + "%") : null
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, count);
    }
}
