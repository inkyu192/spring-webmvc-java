package com.toy.shopwebmvc.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shopwebmvc.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public List<Member> findAllWithJpql(Pageable pageable, String account, String name) {
        String jpql = "SELECT m FROM Member m";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(account)) whereCondition.add("m.account LIKE CONCAT('%', :account, '%')");
        if (StringUtils.hasText(name)) whereCondition.add("m.name LIKE CONCAT('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            jpql += " WHERE ";
            jpql += String.join(" AND ", whereCondition);
        }

        jpql += " LIMIT :offset, :pageSize";

        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);

        if (StringUtils.hasText(account)) query.setParameter("account", account);
        if (StringUtils.hasText(name)) query.setParameter("name", name);
        query.setParameter("pageSize", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());

        return query.getResultList();
    }

    @Override
    public List<Member> findAllWithQuerydsl(Pageable pageable, String account, String name) {
        return queryFactory
                .select(member)
                .from(member)
                .where(
                        account(account),
                        name(name)
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression account(String account) {
        if (StringUtils.hasText(account)) {
            return member.account.like("%" + account + "%");
        }

        return null;
    }

    private BooleanExpression name(String name) {
        if (StringUtils.hasText(name)) {
            return member.name.like("%" + name + "%");
        }

        return null;
    }
}
