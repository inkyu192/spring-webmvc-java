package com.toy.shop.repository;

import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.Order;
import com.toy.shop.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    @Query(
        "select o from Order o" +
        " join fetch o.member m" +
        " join fetch o.delivery d" +
        " where (:memberId is null or m.id = :memberId)" +
        " and (:orderStatus is null or o.status = :orderStatus)" +
        " and (:deliveryStatus is null or d.status = :deliveryStatus)"
    )
    List<Order> findAllOfQueryMethod(@Param("memberId") Long memberId, @Param("orderStatus") OrderStatus orderStatus, @Param("deliveryStatus") DeliveryStatus deliveryStatus);
}
