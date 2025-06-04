package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByIdAndMemberId(Long id, Long memberId);
}
