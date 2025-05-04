package spring.webmvc.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Flight;

public interface FlightJpaRepository extends JpaRepository<Flight, Long> {

	Optional<Flight> findByProductId(Long productId);
}