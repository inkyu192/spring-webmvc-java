package spring.webmvc.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Accommodation;

public interface AccommodationJpaRepository extends JpaRepository<Accommodation, Long> {
	Optional<Accommodation> findByProductId(Long productId);
}