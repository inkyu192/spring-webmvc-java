package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.util.SecurityContextUtil;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public Order createOrder(List<Pair<Long, Integer>> productQuantities) {
		Long memberId = SecurityContextUtil.getMemberId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		Order order = Order.create(member);

		Map<Long, Product> productMap = productRepository.findAllById(
				productQuantities.stream()
					.map(Pair::getFirst)
					.toList())
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		for (Pair<Long, Integer> productQuantity : productQuantities) {
			Product product = productMap.get(productQuantity.getFirst());

			if (product == null) {
				throw new EntityNotFoundException(Product.class, productQuantity.getFirst());
			}

			order.addProduct(product, productQuantity.getSecond());
		}

		return orderRepository.save(order);
	}

	public Page<Order> findOrders(Pageable pageable, OrderStatus orderStatus) {
		Long memberId = SecurityContextUtil.getMemberId();
		return orderRepository.findAll(pageable, memberId, orderStatus);

	}

	public Order findOrder(Long id) {
		Long memberId = SecurityContextUtil.getMemberId();

		return orderRepository.findByIdAndMemberId(id, memberId)
			.orElseThrow(() -> new EntityNotFoundException(Order.class, id));
	}

	@Transactional
	public Order cancelOrder(Long id) {
		Long memberId = SecurityContextUtil.getMemberId();

		Order order = orderRepository.findByIdAndMemberId(id, memberId)
			.orElseThrow(() -> new EntityNotFoundException(Order.class, id));

		order.cancel();

		return order;
	}
}
