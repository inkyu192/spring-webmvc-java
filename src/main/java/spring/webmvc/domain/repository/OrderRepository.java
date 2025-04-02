package spring.webmvc.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.Order;
import spring.webmvc.domain.model.enums.OrderStatus;

public interface OrderRepository {

	Page<Order> findAll(Pageable pageable, Long memberId, OrderStatus orderStatus);

	Optional<Order> findById(Long id);

	Order save(Order order);
}
