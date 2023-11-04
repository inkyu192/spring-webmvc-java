package com.toy.shopwebmvc.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shopwebmvc.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shopwebmvc.domain.QItem.item;

@Repository
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public ItemCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Item> findAllWithJpql(Pageable pageable, String name) {
        String countJpql = """
                SELECT COUNT(i)
                FROM Item i
                """;
        String contentJpql = """
                SELECT i
                FROM Item i
                JOIN FETCH i.category c
                """;

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(name)) whereCondition.add("i.name LIKE CONCAT('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            countJpql += " WHERE ";
            countJpql += String.join(" AND ", whereCondition);

            contentJpql += " WHERE ";
            contentJpql += String.join(" AND ", whereCondition);
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        TypedQuery<Item> contentQuery = entityManager.createQuery(contentJpql, Item.class);

        if (StringUtils.hasText(name)){
            countQuery.setParameter("name", name);
            contentQuery.setParameter("name", name);
        }

        contentQuery.setFirstResult((int) pageable.getOffset());
        contentQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(contentQuery.getResultList(), pageable, countQuery.getSingleResult());
    }

    @Override
    public Page<Item> findAllWithQuerydsl(Pageable pageable, String name) {
        int count = queryFactory
                .selectOne()
                .from(item)
                .where(
                        StringUtils.hasText(name) ? item.name.like("%" + name + "%") : null
                )
                .fetch()
                .size();

        List<Item> content = queryFactory
                .select(item)
                .from(item)
                .where(
                        StringUtils.hasText(name) ? item.name.like("%" + name + "%") : null
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(content, pageable, count);
    }
}
