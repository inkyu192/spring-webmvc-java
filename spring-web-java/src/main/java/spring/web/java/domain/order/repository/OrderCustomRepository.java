package spring.web.java.domain.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.order.Delivery;
import spring.web.java.domain.order.Order;

public interface OrderCustomRepository {

	Page<Order> findAllUsingJpql(
		Pageable pageable, Long memberId, Order.Status orderStatus, Delivery.Status deliveryStatus
	);

	Page<Order> findAllUsingQueryDsl(
		Pageable pageable, Long memberId, Order.Status orderStatus, Delivery.Status deliveryStatus
	);
}
