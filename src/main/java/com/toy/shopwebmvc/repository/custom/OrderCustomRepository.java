package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.constant.DeliveryStatus;
import com.toy.shopwebmvc.constant.OrderStatus;
import com.toy.shopwebmvc.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> findAllWithJpql(
            Pageable pageable,
            Long memberId,
            OrderStatus orderStatus,
            DeliveryStatus deliveryStatus
    );

    Page<Order> findAllWithQuerydsl(
            Pageable pageable,
            Long memberId,
            OrderStatus orderStatus,
            DeliveryStatus deliveryStatus
    );
}
