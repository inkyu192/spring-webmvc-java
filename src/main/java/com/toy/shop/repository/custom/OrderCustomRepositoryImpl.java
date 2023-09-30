package com.toy.shop.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.Order;
import com.toy.shop.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.domain.QDelivery.delivery;
import static com.toy.shop.domain.QMember.member;
import static com.toy.shop.domain.QOrder.order;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public OrderCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Order> findAllOfJpql(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus) {
        String jpql = "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d";

        ArrayList<String> whereCondition = new ArrayList<>();

        if (memberId != null) whereCondition.add("m.id = :memberId");
        if (orderStatus != null) whereCondition.add("o.status = :orderStatus");
        if (deliveryStatus != null) whereCondition.add("d.status = :deliveryStatus");

        if (!whereCondition.isEmpty()) {
            jpql += " where ";
            jpql += String.join(" and ", whereCondition);
        }

        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class);

        if (memberId != null) query.setParameter("memberId", memberId);
        if (orderStatus != null) query.setParameter("orderStatus", orderStatus);
        if (deliveryStatus != null) query.setParameter("deliveryStatus", deliveryStatus);

        return query.getResultList();
    }

    @Override
    public List<Order> findAllOfQuery(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus) {
        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member)
                .fetchJoin()
                .join(order.delivery, delivery)
                .fetchJoin()
                .where(
                        memberId(memberId),
                        orderStatus(orderStatus),
                        deliveryStatus(deliveryStatus)
                )
                .fetch();
    }

    private BooleanExpression memberId(Long memberId) {
        if (memberId != null) {
            return member.id.eq(memberId);
        }
        return null;
    }

    private BooleanExpression orderStatus(OrderStatus orderStatus) {
        if (orderStatus != null) {
            return order.status.eq(orderStatus);
        }
        return null;
    }

    private BooleanExpression deliveryStatus(DeliveryStatus deliveryStatus) {
        if (deliveryStatus != null) {
            return delivery.status.eq(deliveryStatus);
        }
        return null;
    }
}
