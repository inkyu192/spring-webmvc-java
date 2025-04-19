package spring.webmvc.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Flight;

public interface FlightJpaRepository extends JpaRepository<Flight, Long> {
}