package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.constant.DeliveryStatus;
import com.toy.shopwebmvc.domain.Order;
import com.toy.shopwebmvc.constant.OrderStatus;

import java.util.List;

public interface OrderCustomRepository {

    List<Order> findAllWithJpql(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);

    List<Order> findAllWithQuerydsl(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);
}
