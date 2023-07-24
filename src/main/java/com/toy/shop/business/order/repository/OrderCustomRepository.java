package com.toy.shop.business.order.repository;

import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.Order;
import com.toy.shop.domain.OrderStatus;

import java.util.List;

public interface OrderCustomRepository {

    List<Order> findAllOfJpql(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);

    List<Order> findAllOfQuery(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);
}
