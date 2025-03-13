package spring.web.java.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.model.entity.Order;
import spring.web.java.domain.model.enums.OrderStatus;

public interface OrderRepository {

	Page<Order> findAll(Pageable pageable, Long memberId, OrderStatus orderStatus);

	Optional<Order> findById(Long id);

	Order save(Order order);
}
