package com.toy.shop.business.order.service;

import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.OrderStatus;
import com.toy.shop.business.order.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto.Response saveOrder(OrderDto.Save requestDto);

    Page<OrderDto.Response> orders(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable);

    OrderDto.Response order(Long id);

    OrderDto.Response cancelOrder(Long id);
}
