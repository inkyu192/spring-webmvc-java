package spring.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.web.java.constant.ApiResponseCode;
import spring.web.java.constant.DeliveryStatus;
import spring.web.java.constant.OrderStatus;
import spring.web.java.domain.*;
import spring.web.java.dto.request.OrderSaveRequest;
import spring.web.java.dto.response.OrderResponse;
import spring.web.java.exception.CommonException;
import spring.web.java.repository.ItemRepository;
import spring.web.java.repository.MemberRepository;
import spring.web.java.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;



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
                .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();

        orderSaveRequest.orderItems().forEach(orderItem -> {
            Item item = itemRepository.findById(orderItem.itemId())
                    .orElseThrow(() -> new CommonException(ApiResponseCode.DATA_NOT_FOUND));

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
        return orderRepository.findAllWithQuerydsl(pageable, memberId, orderStatus, deliveryStatus)
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
