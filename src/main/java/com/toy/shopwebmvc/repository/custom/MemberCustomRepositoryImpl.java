package com.toy.shopwebmvc.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shopwebmvc.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shopwebmvc.domain.QMember.member;

@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findAllWithJpql(Pageable pageable, String account, String name) {
        String countJpql = "SELECT COUNT(m) FROM Member m";
        String contentJpql = "SELECT m FROM Member m";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(account)) whereCondition.add("m.account LIKE CONCAT('%', :account, '%')");
        if (StringUtils.hasText(name)) whereCondition.add("m.name LIKE CONCAT('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            countJpql += " WHERE ";
            countJpql += String.join(" AND ", whereCondition);

            contentJpql += " WHERE ";
            contentJpql += String.join(" AND ", whereCondition);
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        TypedQuery<Member> contentQuery = entityManager.createQuery(contentJpql, Member.class);

        if (StringUtils.hasText(account)){
            countQuery.setParameter("account", account);
            contentQuery.setParameter("account", account);
        }

        if (StringUtils.hasText(name)){
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
                .select(member)
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
