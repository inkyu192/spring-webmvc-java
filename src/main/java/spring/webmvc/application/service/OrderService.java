package spring.webmvc.application.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.OrderCreateCommand;
import spring.webmvc.application.dto.command.OrderProductCreateCommand;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.exception.EntityNotFoundException;
import spring.webmvc.presentation.exception.InsufficientQuantityException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final ValueCache valueCache;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public Order createOrder(OrderCreateCommand orderCreateCommand) {
		Long memberId = SecurityContextUtil.getMemberId();
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		Map<Long, Product> productMap = productRepository.findAllById(
				orderCreateCommand.products().stream()
					.map(OrderProductCreateCommand::productId)
					.toList()
			)
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		Order order = Order.create(member);

		for (OrderProductCreateCommand orderProductCreateCommand : orderCreateCommand.products()) {
			Product product = productMap.get(orderProductCreateCommand.productId());
			int quantity = orderProductCreateCommand.quantity();

			if (product == null) {
				throw new EntityNotFoundException(Product.class, orderProductCreateCommand.productId());
			}

			String key = CacheKey.PRODUCT_STOCK.generate(product.getId());
			Long stock = valueCache.decrement(key, quantity);

			if (stock == null || stock < 0) {
				if (stock != null) {
					valueCache.increment(key, quantity);
				}
				throw new InsufficientQuantityException(product.getName(), quantity, valueCache.get(key, Long.class));
			}

			order.addProduct(product, quantity);
		}

		try {
			return orderRepository.save(order);
		} catch (Exception e) {
			for (OrderProductCreateCommand orderProductCreateCommand : orderCreateCommand.products()) {
				String key = CacheKey.PRODUCT_STOCK.generate(orderProductCreateCommand.productId());
				valueCache.increment(key, orderProductCreateCommand.quantity());
			}
			throw e;
		}
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
