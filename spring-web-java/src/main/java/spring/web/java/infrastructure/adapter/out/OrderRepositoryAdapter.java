package spring.web.java.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.out.OrderJpaCustomRepositoryPort;
import spring.web.java.application.port.out.OrderJpaRepositoryPort;
import spring.web.java.application.port.out.OrderQueryDslRepositoryPort;
import spring.web.java.application.port.out.OrderRepositoryPort;
import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.domain.Order;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

	private final OrderJpaRepositoryPort jpaRepository;
	private final OrderJpaCustomRepositoryPort jpaCustomRepository;
	private final OrderQueryDslRepositoryPort queryDslRepository;

	@Override
	public Order save(Order order) {
		return jpaRepository.save(order);
	}

	@Override
	public Page<Order> findAll(
		Pageable pageable, Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus
	) {
		// return jpaCustomRepository.findAll(pageable, memberId, orderStatus, deliveryStatus);
		return queryDslRepository.findAll(pageable, memberId, orderStatus, deliveryStatus);
	}

	@Override
	public Optional<Order> findById(Long id) {
		return jpaRepository.findById(id);
	}
}
