package spring.webmvc.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
