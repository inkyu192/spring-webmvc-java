package spring.webmvc.application.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.OrderCreateCommand;
import spring.webmvc.application.dto.command.OrderProductCreateCommand;
import spring.webmvc.application.dto.command.OrderStatusUpdateCommand;
import spring.webmvc.application.dto.query.OrderCursorPageQuery;
import spring.webmvc.application.dto.query.OrderOffsetPageQuery;
import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.repository.OrderRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.domain.repository.cache.OrderCacheRepository;
import spring.webmvc.domain.repository.cache.ProductCacheRepository;
import spring.webmvc.infrastructure.exception.InsufficientQuantityException;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;
import spring.webmvc.infrastructure.exception.OrderNumberGenerationException;
import spring.webmvc.infrastructure.security.SecurityContextUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	private final OrderCacheRepository orderCacheRepository;
	private ProductCacheRepository productCacheRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public OrderDetailResult createOrder(OrderCreateCommand command) {
		User user = userRepository.findById(command.userId())
			.orElseThrow(() -> new NotFoundEntityException(User.class, command.userId()));

		List<Long> productIds = command.products().stream()
			.map(OrderProductCreateCommand::id)
			.toList();

		Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		String orderNumber = generateOrderNumber();
		Order order = Order.create(orderNumber, user);
		List<OrderProductCreateCommand> processedProducts = new ArrayList<>();

		try {
			for (OrderProductCreateCommand orderProductCreateCommand : command.products()) {
				Long productId = orderProductCreateCommand.id();

				Product product = productMap.get(productId);
				if (product == null) {
					throw new NotFoundEntityException(Product.class, productId);
				}

				// 캐시 없을 경우 초기화
				if (productCacheRepository.getProductStock(productId) == null) {
					productCacheRepository.setProductStockIfAbsent(productId, product.getQuantity(),
						Duration.ofHours(1));
				}

				// 원자적 재고 감소 처리
				Long stock = productCacheRepository.decrementProductStock(productId,
					orderProductCreateCommand.quantity());

				if (stock == null || stock < 0) {
					if (stock != null) {
						productCacheRepository.incrementProductStock(productId, orderProductCreateCommand.quantity());
					}

					Long currentStock = productCacheRepository.getProductStock(productId);
					long safeStock = currentStock != null ? currentStock : 0L;

					throw new InsufficientQuantityException(
						product.getName(),
						orderProductCreateCommand.quantity(),
						safeStock
					);
				}

				// 성공 시 주문 처리
				order.addProduct(product, orderProductCreateCommand.quantity());
				processedProducts.add(orderProductCreateCommand);
			}

			orderRepository.save(order);

			return OrderDetailResult.of(order);
		} catch (Exception e) {
			for (OrderProductCreateCommand orderProductCreateCommand : processedProducts) {
				productCacheRepository.incrementProductStock(
					orderProductCreateCommand.id(),
					orderProductCreateCommand.quantity()
				);
			}
			throw e;
		}
	}

	public Page<OrderSummaryResult> findOrdersWithOffsetPage(OrderOffsetPageQuery query) {
		return orderRepository.findAllWithOffsetPage(
				query.pageable(),
				query.userId(),
				query.orderStatus(),
				query.orderedFrom(),
				query.orderedTo()
			)
			.map(OrderSummaryResult::of);
	}

	public CursorPage<OrderSummaryResult> findOrdersWithCursorPage(OrderCursorPageQuery query) {
		return orderRepository.findAllWithCursorPage(
				query.cursorId(),
				query.userId(),
				query.orderStatus(),
				query.orderedFrom(),
				query.orderedTo()
			)
			.map(OrderSummaryResult::of);
	}

	public OrderDetailResult findOrder(Long id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(Order.class, id));

		return OrderDetailResult.of(order);
	}

	public OrderDetailResult findOrderByUser(Long id, Long userId) {
		Order order = orderRepository.findByIdAndUserId(id, userId)
			.orElseThrow(() -> new NotFoundEntityException(Order.class, id));

		return OrderDetailResult.of(order);
	}

	@Transactional
	public OrderDetailResult updateOrderStatus(OrderStatusUpdateCommand command) {
		Order order = orderRepository.findById(command.id())
			.orElseThrow(() -> new NotFoundEntityException(Order.class, command.id()));

		order.updateStatus(command.orderStatus());

		return OrderDetailResult.of(order);
	}

	@Transactional
	public OrderDetailResult cancelOrder(Long id) {
		Long userId = SecurityContextUtil.getUserId();
		Order order = orderRepository.findByIdAndUserId(id, userId)
			.orElseThrow(() -> new NotFoundEntityException(Order.class, id));

		order.cancel();

		return OrderDetailResult.of(order);
	}

	private String generateOrderNumber() {
		String date = LocalDate.now().format(DATE_FORMATTER);

		Long seq = orderCacheRepository.incrementSequence(date);
		if (seq == null) {
			throw new OrderNumberGenerationException();
		}

		Long finalSeq;
		if (seq == 1L) {
			Long lastSeq = recoverSequenceFromDb(date);
			if (lastSeq > 0) {
				orderCacheRepository.setSequence(date, lastSeq + 1);
				finalSeq = lastSeq + 1;
			} else {
				finalSeq = seq;
			}
		} else {
			finalSeq = seq;
		}

		return date + String.format("%08d", finalSeq);
	}

	private Long recoverSequenceFromDb(String date) {
		String maxOrderNumber = orderRepository.findMaxOrderNumberByDate(date);
		if (maxOrderNumber != null && maxOrderNumber.length() >= 8) {
			String seqPart = maxOrderNumber.substring(maxOrderNumber.length() - 8);
			try {
				return Long.parseLong(seqPart);
			} catch (NumberFormatException e) {
				return 0L;
			}
		}
		return 0L;
	}
}
