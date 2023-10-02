package com.toy.shopwebmvc.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shopwebmvc.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public List<Item> findAllOfJpql(String name) {
        String jpql = "select i from Item i" +
                " join fetch i.category c";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (StringUtils.hasText(name)) whereCondition.add("i.name like concat('%', :name, '%')");

        if (!whereCondition.isEmpty()) {
            jpql += " where ";
            jpql += String.join(" and ", whereCondition);
        }

        TypedQuery<Item> query = entityManager.createQuery(jpql, Item.class);

        if (StringUtils.hasText(name)) query.setParameter("name", name);

        return query.getResultList();
    }

    @Override
    public List<Item> findAllOfQuery(String name) {
        return queryFactory
                .select(item)
                .from(item)
                .where(name(name))
                .fetch();
    }

    private BooleanExpression name(String name) {
        if (StringUtils.hasText(name)) {
            return item.name.like("%" + name + "%");
        }
        return null;
    }
}
