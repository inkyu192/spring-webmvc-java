package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Transport;

public interface TransportJpaRepository extends JpaRepository<Transport, Long> {
	Optional<Transport> findByProductId(Long productId);
}
