package spring.webmvc.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.FlightDto;
import spring.webmvc.domain.cache.FlightCache;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;
	private final FlightCache flightCache;
	private final JsonSupport jsonSupport;

	public FlightDto findFlight(Long id) {
		Optional<FlightDto> optionalFlightDto = flightCache.get(id)
			.flatMap(value -> jsonSupport.readValue(value, FlightDto.class));

		if (optionalFlightDto.isPresent()) {
			return optionalFlightDto.get();
		}

		FlightDto flightDto = flightRepository.findById(id)
			.map(FlightDto::new)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		jsonSupport.writeValueAsString(flightDto).ifPresent(value -> flightCache.set(id, value));

		return flightDto;
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
