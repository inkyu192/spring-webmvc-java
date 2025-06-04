package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.infrastructure.persistence.jpa.FlightJpaRepository;

@Component
@RequiredArgsConstructor
public class FlightRepositoryAdapter implements FlightRepository {

	private final FlightJpaRepository jpaRepository;

	@Override
	public Optional<Flight> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Flight> findByProductId(Long productId) {
		return jpaRepository.findByProductId(productId);
	}

	@Override
	public Flight save(Flight flight) {
		return jpaRepository.save(flight);
	}

	@Override
	public void delete(Flight flight) {
		jpaRepository.delete(flight);
	}
}