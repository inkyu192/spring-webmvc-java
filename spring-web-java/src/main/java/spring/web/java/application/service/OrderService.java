package spring.web.java.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Address;
import spring.web.java.domain.model.entity.Delivery;
import spring.web.java.domain.model.entity.Item;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.entity.Order;
import spring.web.java.domain.model.entity.OrderItem;
import spring.web.java.domain.model.enums.DeliveryStatus;
import spring.web.java.domain.model.enums.OrderStatus;
import spring.web.java.domain.repository.ItemRepository;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.domain.repository.OrderRepository;
import spring.web.java.presentation.dto.request.OrderSaveRequest;
import spring.web.java.presentation.dto.response.OrderResponse;
import spring.web.java.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponse saveOrder(OrderSaveRequest orderSaveRequest) {
		Long memberId = orderSaveRequest.memberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		List<OrderItem> orderItems = new ArrayList<>();

		orderSaveRequest.orderItems().forEach(orderItem -> {
			Long itemId = orderItem.itemId();
			Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new EntityNotFoundException(Item.class, itemId));

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
		return orderRepository.findAll(pageable, memberId, orderStatus, deliveryStatus)
			.map(OrderResponse::new);
	}

	public OrderResponse findOrder(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Order.class, id));

		return new OrderResponse(order);
	}

	@Transactional
	public OrderResponse cancelOrder(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Order.class, id));

		order.cancel();

		return new OrderResponse(order);
	}
}
