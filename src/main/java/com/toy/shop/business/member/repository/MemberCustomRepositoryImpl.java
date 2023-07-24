package com.toy.shop.business.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.domain.QMember.member;

@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Member> findAllOfJpql(String name) {
        String jpql = "select m from Member m";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(name)) whereCondition.add("m.name like concat('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            jpql += " where ";
            jpql += String.join(" and ", whereCondition);
        }

        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);

        if (StringUtils.hasText(name)) query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Member> findAllOfQuery(String name) {
        return queryFactory
                .select(member)
                .from(member)
                .where(name(name))
                .fetch();
    }

    private BooleanExpression name(String name) {
        if (StringUtils.hasText(name)) {
            return member.name.like("%" + name + "%");
        }

        return null;
    }
}
