package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.constant.DeliveryStatus;
import com.toy.shopwebmvc.domain.Order;
import com.toy.shopwebmvc.constant.OrderStatus;
import com.toy.shopwebmvc.repository.custom.OrderCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    @Query(
        value =
            "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " where (:memberId is null or m.id = :memberId)" +
            " and (:orderStatus is null or o.status = :orderStatus)" +
            " and (:deliveryStatus is null or d.status = :deliveryStatus)",
        countQuery =
            "select o from Order o" +
            " join o.member m" +
            " join o.delivery d" +
            " where (:memberId is null or m.id = :memberId)" +
            " and (:orderStatus is null or o.status = :orderStatus)" +
            " and (:deliveryStatus is null or d.status = :deliveryStatus)"
    )
    Page<Order> findAllOfQueryMethod(@Param("memberId") Long memberId, @Param("orderStatus") OrderStatus orderStatus, @Param("deliveryStatus") DeliveryStatus deliveryStatus, Pageable pageable);
}
