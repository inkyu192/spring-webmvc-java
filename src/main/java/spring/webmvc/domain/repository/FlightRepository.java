package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Flight;

public interface FlightRepository {

	Optional<Flight> findById(Long id);

	Flight save(Flight flight);

	void delete(Flight flight);
}