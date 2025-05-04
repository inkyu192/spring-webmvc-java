package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Flight;

public interface FlightRepository {

	Optional<Flight> findById(Long id);

	Optional<Flight> findByProductId(Long productId);

	Flight save(Flight flight);

	void delete(Flight flight);
}