package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Flight;

public interface FlightJpaRepository extends JpaRepository<Flight, Long> {

	Optional<Flight> findByProductId(Long productId);
}