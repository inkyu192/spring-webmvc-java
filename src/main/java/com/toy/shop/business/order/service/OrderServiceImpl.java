package com.toy.shop.business.order.service;

import com.toy.shop.business.item.repository.ItemRepository;
import com.toy.shop.business.member.repository.MemberRepository;
import com.toy.shop.business.order.dto.request.OrderSaveRequest;
import com.toy.shop.business.order.dto.response.OrderResponse;
import com.toy.shop.business.order.repository.OrderRepository;
import com.toy.shop.common.ApiResponseCode;
import com.toy.shop.domain.*;
import com.toy.shop.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.common.ApiResponseCode.ITEM_NOT_FOUND;
import static com.toy.shop.common.ApiResponseCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse saveOrder(OrderSaveRequest orderSaveRequest) {
        Member member = memberRepository.findById(orderSaveRequest.memberId())
                .orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();

        orderSaveRequest.orderItems().forEach(orderItem -> {
            Item item = itemRepository.findById(orderItem.itemId())
                    .orElseThrow(() -> new CommonException(ITEM_NOT_FOUND));

            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItem.count()));
        });

        Delivery delivery = Delivery.createDelivery(orderSaveRequest);

        Order order = Order.createOrder(member, delivery, orderItems);

        orderRepository.save(order);

        return new OrderResponse(order);
    }

    @Override
    public Page<OrderResponse> orders(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable) {
        return orderRepository.findAllOfQueryMethod(memberId, orderStatus, deliveryStatus, pageable)
                .map(OrderResponse::new);
    }

    @Override
    public OrderResponse order(Long id) {
        return orderRepository.findById(id)
                .map(OrderResponse::new)
                .orElseThrow(() -> new CommonException(ApiResponseCode.ORDER_NOT_FOUND));
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.ORDER_NOT_FOUND));

        order.cancel();

        return new OrderResponse(order);
    }
}
