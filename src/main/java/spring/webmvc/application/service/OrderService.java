package spring.webmvc.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Item;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.OrderItem;
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

		List<OrderItem> orderItems = new ArrayList<>();
		if (!ObjectUtils.isEmpty(orderSaveRequest.orderItems())) {
			for (OrderItemSaveRequest requestOrderIterm : orderSaveRequest.orderItems()) {
				Long itemId = requestOrderIterm.itemId();
				Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new EntityNotFoundException(Item.class, itemId));

				orderItems.add(OrderItem.create(item, requestOrderIterm.count()));
			}
		}

		Order order = orderRepository.save(
			Order.create(member, orderItems)
		);

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
