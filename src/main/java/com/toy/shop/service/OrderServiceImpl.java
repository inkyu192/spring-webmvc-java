package com.toy.shop.service;

import com.toy.shop.common.ResultCode;
import com.toy.shop.domain.*;
import com.toy.shop.dto.OrderDto;
import com.toy.shop.exception.CommonException;
import com.toy.shop.repository.ItemRepository;
import com.toy.shop.repository.MemberRepository;
import com.toy.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.toy.shop.common.ResultCode.ITEM_NOT_FOUND;
import static com.toy.shop.common.ResultCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderDto.Response saveOrder(OrderDto.Save requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new CommonException(MEMBER_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();

        requestDto.getOrderItems().forEach(orderItem -> {
            Item item = itemRepository.findById(orderItem.getItemId()).orElseThrow(() -> new CommonException(ITEM_NOT_FOUND));

            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItem.getCount()));
        });

        Delivery delivery = Delivery.createDelivery(requestDto);

        Order order = Order.createOrder(member, delivery, orderItems);

        orderRepository.save(order);

        return new OrderDto.Response(order);
    }

    @Override
    public Page<OrderDto.Response> orders(Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable) {
        Page<Order> page = orderRepository.findAllOfQueryMethod(memberId, orderStatus, deliveryStatus, pageable);

        return page.map(OrderDto.Response::new);
    }

    @Override
    public OrderDto.Response order(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CommonException(ResultCode.ORDER_NOT_FOUND));

        return new OrderDto.Response(order);
    }

    @Override
    public OrderDto.Response cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new CommonException(ResultCode.ORDER_NOT_FOUND));

        order.cancel();

        return new OrderDto.Response(order);
    }
}
