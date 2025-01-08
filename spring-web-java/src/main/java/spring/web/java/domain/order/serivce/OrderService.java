package spring.web.java.domain.order.serivce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Address;
import spring.web.java.domain.item.Item;
import spring.web.java.domain.item.repository.ItemRepository;
import spring.web.java.domain.member.Member;
import spring.web.java.domain.member.repository.MemberRepository;
import spring.web.java.domain.order.Delivery;
import spring.web.java.domain.order.Order;
import spring.web.java.domain.order.OrderItem;
import spring.web.java.domain.order.dto.OrderResponse;
import spring.web.java.domain.order.dto.OrderSaveRequest;
import spring.web.java.domain.order.repository.OrderRepository;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.exception.DomainException;

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

	public Page<OrderResponse> findOrders(
		Long memberId,
		Order.Status orderStatus,
		Delivery.Status deliveryStatus,
		Pageable pageable
	) {
		return orderRepository.findAllUsingJpql(pageable, memberId, orderStatus, deliveryStatus)
			.map(OrderResponse::new);
	}

	public OrderResponse findOrder(Long id) {
		return orderRepository.findById(id)
			.map(OrderResponse::new)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	public OrderResponse cancelOrder(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));

		order.cancel();

		return new OrderResponse(order);
	}
}
