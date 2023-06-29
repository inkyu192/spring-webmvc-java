package com.toy.shop.service;

import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.OrderStatus;
import com.toy.shop.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderDto.Response saveOrder(OrderDto.Save requestDto);

    Page<OrderDto.Response> orders(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable);

    OrderDto.Response order(Long id);

    OrderDto.Response cancelOrder(Long id);
}
