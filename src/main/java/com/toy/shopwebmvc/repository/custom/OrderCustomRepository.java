package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.constant.DeliveryStatus;
import com.toy.shopwebmvc.domain.Order;
import com.toy.shopwebmvc.constant.OrderStatus;

import java.util.List;

public interface OrderCustomRepository {

    List<Order> findAllOfJpql(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);

    List<Order> findAllOfQuery(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);
}
