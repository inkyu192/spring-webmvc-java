package com.toy.shop.business.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shop.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.domain.QCategory.category;
import static com.toy.shop.domain.QItem.item;

@Repository
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public ItemCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Item> findAllOfJpql(Long categoryId, String name) {
        String jpql = "select i from Item i" +
                " join fetch i.category c";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (categoryId != null) whereCondition.add("c.id = :categoryId");
        if (StringUtils.hasText(name)) whereCondition.add("i.name like concat('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            jpql += " where ";
            jpql += String.join(" and ", whereCondition);
        }

        TypedQuery<Item> query = entityManager.createQuery(jpql, Item.class);

        if (categoryId != null) query.setParameter("categoryId", categoryId);
        if (StringUtils.hasText(name)) query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Item> findAllOfQuery(Long categoryId, String name) {
        return queryFactory
                .select(item)
                .from(item)
                .join(item.category, category)
                .fetchJoin()
                .where(
                        categoryId(categoryId),
                        name(name)
                )
                .fetch();
    }

    private BooleanExpression categoryId(Long categoryId) {
        if (categoryId != null) {
            return category.id.eq(categoryId);
        }
        return null;
    }

    private BooleanExpression name(String name) {
        if (StringUtils.hasText(name)) {
            return item.name.like("%" + name + "%");
        }
        return null;
    }
}
