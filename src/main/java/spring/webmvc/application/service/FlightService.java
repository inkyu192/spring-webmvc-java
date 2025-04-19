package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.dto.request.FlightCreateRequest;
import spring.webmvc.presentation.dto.request.FlightUpdateRequest;
import spring.webmvc.presentation.dto.response.FlightResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlightService {

	private final FlightRepository flightRepository;

	public FlightResponse findFlight(Long id) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		return new FlightResponse(flight);
	}

	@Transactional
	public FlightResponse createFlight(FlightCreateRequest flightCreateRequest) {
		Flight flight = flightRepository.save(
			Flight.create(
				flightCreateRequest.name(),
				flightCreateRequest.description(),
				flightCreateRequest.price(),
				flightCreateRequest.quantity(),
				flightCreateRequest.airline(),
				flightCreateRequest.flightNumber(),
				flightCreateRequest.departureAirport(),
				flightCreateRequest.arrivalAirport(),
				flightCreateRequest.departureTime(),
				flightCreateRequest.arrivalTime()
			)
		);

		return new FlightResponse(flight);
	}

	@Transactional
	public FlightResponse updateFlight(Long id, FlightUpdateRequest flightUpdateRequest) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flight.update(
			flightUpdateRequest.name(),
			flightUpdateRequest.description(),
			flightUpdateRequest.price(),
			flightUpdateRequest.quantity(),
			flightUpdateRequest.airline(),
			flightUpdateRequest.flightNumber(),
			flightUpdateRequest.departureAirport(),
			flightUpdateRequest.arrivalAirport(),
			flightUpdateRequest.departureTime(),
			flightUpdateRequest.arrivalTime()
		);

		return new FlightResponse(flight);
	}

	@Transactional
	public void deleteFlight(Long id) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flightRepository.delete(flight);
	}
}
