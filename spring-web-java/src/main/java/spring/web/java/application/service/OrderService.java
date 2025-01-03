package spring.web.java.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.OrderServicePort;
import spring.web.java.application.port.out.ItemRepositoryPort;
import spring.web.java.application.port.out.MemberRepositoryPort;
import spring.web.java.application.port.out.OrderRepositoryPort;
import spring.web.java.common.ResponseMessage;
import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.domain.Address;
import spring.web.java.domain.Delivery;
import spring.web.java.domain.Item;
import spring.web.java.domain.Member;
import spring.web.java.domain.Order;
import spring.web.java.domain.OrderItem;
import spring.web.java.dto.request.OrderSaveRequest;
import spring.web.java.dto.response.OrderResponse;
import spring.web.java.infrastructure.configuration.exception.DomainException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService implements OrderServicePort {

	private final MemberRepositoryPort memberRepository;
	private final ItemRepositoryPort itemRepository;
	private final OrderRepositoryPort orderRepository;

	@Override
	@Transactional
	public OrderResponse saveOrder(OrderSaveRequest orderSaveRequest) {
		Member member = memberRepository.findById(orderSaveRequest.memberId())
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<OrderItem> orderItems = new ArrayList<>();

		orderSaveRequest.orderItems().forEach(orderItem -> {
			Item item = itemRepository.findById(orderItem.itemId())
				.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

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

		return new OrderResponse(orderRepository.save(order));
	}

	@Override
	public Page<OrderResponse> findOrders(
		Long memberId,
		OrderStatus orderStatus,
		DeliveryStatus deliveryStatus,
		Pageable pageable
	) {
		return orderRepository.findAll(pageable, memberId, orderStatus, deliveryStatus)
			.map(OrderResponse::new);
	}

	@Override
	public OrderResponse findOrder(Long id) {
		return orderRepository.findById(id)
			.map(OrderResponse::new)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	@Override
	public OrderResponse cancelOrder(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		order.cancel();

		return new OrderResponse(order);
	}
}
