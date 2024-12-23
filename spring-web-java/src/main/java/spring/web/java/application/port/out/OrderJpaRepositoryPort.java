package spring.web.java.application.port.out;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.Order;

public interface OrderJpaRepositoryPort extends JpaRepository<Order, Long> {
}
