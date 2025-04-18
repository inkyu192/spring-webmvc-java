package spring.webmvc.application.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.dto.request.OrderProductCreateRequest;
import spring.webmvc.presentation.dto.request.OrderCreateRequest;
import spring.webmvc.presentation.dto.response.OrderResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
		Long memberId = orderCreateRequest.memberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		Order order = Order.create(member);

		if (!ObjectUtils.isEmpty(orderCreateRequest.orderProducts())) {
			Map<Long, Product> productMap = productRepository.findAllById(
					orderCreateRequest.orderProducts()
						.stream()
						.map(OrderProductCreateRequest::productId)
						.toList())
				.stream()
				.collect(Collectors.toMap(Product::getId, product -> product));

			for (OrderProductCreateRequest requestOrderIterm : orderCreateRequest.orderProducts()) {
				Product product = productMap.get(requestOrderIterm.productId());

				if (product == null) {
					throw new EntityNotFoundException(Product.class, requestOrderIterm.productId());
				}

				order.addProduct(product, requestOrderIterm.count());
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
