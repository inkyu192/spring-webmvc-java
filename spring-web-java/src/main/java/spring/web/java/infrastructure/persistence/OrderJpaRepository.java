package spring.web.java.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
