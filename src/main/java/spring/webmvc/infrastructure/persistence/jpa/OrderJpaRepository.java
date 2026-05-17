package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import spring.webmvc.domain.model.entity.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByIdAndUserId(Long id, Long userId);

	@Query("SELECT MAX(o.orderNumber) FROM Order o WHERE o.orderNumber LIKE :datePrefix%")
	String findMaxOrderNumberByDatePrefix(@Param("datePrefix") String datePrefix);
}
