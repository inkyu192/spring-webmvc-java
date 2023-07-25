package com.toy.shop.business.order.service;

import com.toy.shop.business.order.dto.request.OrderSaveRequest;
import com.toy.shop.business.order.dto.response.OrderResponse;
import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse saveOrder(OrderSaveRequest orderSaveRequest);

    Page<OrderResponse> orders(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable);

    OrderResponse order(Long id);

    OrderResponse cancelOrder(Long id);
}
