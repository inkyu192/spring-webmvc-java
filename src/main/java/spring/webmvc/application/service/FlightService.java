package spring.webmvc.application.service;

import java.time.Instant;

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

	public Flight findFlight(Long id) {
		return flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));
	}

	@Transactional
	public Flight createFlight(
		String name,
		String description,
		int price,
		int quantity,
		String airline,
		String flightNumber,
		String departureAirport,
		String arrivalAirport,
		Instant departureTime,
		Instant arrivalTime
	) {
		return flightRepository.save(
			Flight.create(
				name,
				description,
				price,
				quantity,
				airline,
				flightNumber,
				departureAirport,
				arrivalAirport,
				departureTime,
				arrivalTime
			)
		);
	}

	@Transactional
	public Flight updateFlight(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		String airline,
		String flightNumber,
		String departureAirport,
		String arrivalAirport,
		Instant departureTime,
		Instant arrivalTime
	) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flight.update(
			name,
			description,
			price,
			quantity,
			airline,
			flightNumber,
			departureAirport,
			arrivalAirport,
			departureTime,
			arrivalTime
		);

		return flight;
	}

	@Transactional
	public void deleteFlight(Long id) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flightRepository.delete(flight);
	}
}
