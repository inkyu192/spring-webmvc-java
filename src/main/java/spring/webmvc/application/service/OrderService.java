package spring.webmvc.application.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Item;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.domain.repository.ItemRepository;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.presentation.dto.request.OrderItemSaveRequest;
import spring.webmvc.presentation.dto.request.OrderSaveRequest;
import spring.webmvc.presentation.dto.response.OrderResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

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

		Order order = Order.create(member);

		if (!ObjectUtils.isEmpty(orderSaveRequest.orderItems())) {
			Map<Long, Item> itemMap = itemRepository.findAllById(
					orderSaveRequest.orderItems()
						.stream()
						.map(OrderItemSaveRequest::itemId)
						.toList())
				.stream()
				.collect(Collectors.toMap(Item::getId, item -> item));

			for (OrderItemSaveRequest requestOrderIterm : orderSaveRequest.orderItems()) {
				Item item = itemMap.get(requestOrderIterm.itemId());

				if (item == null) {
					throw new EntityNotFoundException(Item.class, requestOrderIterm.itemId());
				}

				order.addItem(item, requestOrderIterm.count());
			}
		}

		orderRepository.save(order);

		return new OrderResponse(order);
	}

	public Page<OrderResponse> findOrders(
		Long memberId,
		OrderStatus orderStatus,
		Pageable pageable
	) {
		return orderRepository.findAll(pageable, memberId, orderStatus)
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
