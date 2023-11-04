package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.constant.DeliveryStatus;
import com.toy.shopwebmvc.constant.OrderStatus;
import com.toy.shopwebmvc.domain.*;
import com.toy.shopwebmvc.dto.request.OrderSaveRequest;
import com.toy.shopwebmvc.dto.response.OrderResponse;
import com.toy.shopwebmvc.exception.CommonException;
import com.toy.shopwebmvc.repository.ItemRepository;
import com.toy.shopwebmvc.repository.MemberRepository;
import com.toy.shopwebmvc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shopwebmvc.constant.ApiResponseCode.DATA_NOT_FOUND;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse saveOrder(OrderSaveRequest orderSaveRequest) {
        Member member = memberRepository.findById(orderSaveRequest.memberId())
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();

        orderSaveRequest.orderItems().forEach(orderItem -> {
            Item item = itemRepository.findById(orderItem.itemId())
                    .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

            orderItems.add(OrderItem.create(item, item.getPrice(), orderItem.count()));
        });

        Delivery delivery = Delivery.create(
                Address.create(
                        orderSaveRequest.city(),
                        orderSaveRequest.street(),
                        orderSaveRequest.zipcode()
                )
        );

        Order order = Order.create(member, delivery, orderItems);

        orderRepository.save(order);

        return new OrderResponse(order);
    }

    public Page<OrderResponse> findOrders(
            Long memberId,
            OrderStatus orderStatus,
            DeliveryStatus deliveryStatus,
            Pageable pageable
    ) {
        return orderRepository.findAll(memberId, orderStatus, deliveryStatus, pageable)
                .map(OrderResponse::new);
    }

    public OrderResponse findOrder(Long id) {
        return orderRepository.findById(id)
                .map(OrderResponse::new)
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));

        order.cancel();

        return new OrderResponse(order);
    }
}
