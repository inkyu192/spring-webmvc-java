package spring.web.java.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
}
