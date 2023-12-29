package com.webmvc.javaapi.repository.custom;

import com.webmvc.javaapi.constant.DeliveryStatus;
import com.webmvc.javaapi.constant.OrderStatus;
import com.webmvc.javaapi.domain.Order;
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
