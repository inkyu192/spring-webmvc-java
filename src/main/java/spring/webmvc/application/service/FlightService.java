package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

	@Transactional
	public void deleteFlight(Long id) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flightRepository.delete(flight);
	}
}
