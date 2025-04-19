package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.dto.request.FlightSaveRequest;
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
	public FlightResponse createFlight(FlightSaveRequest flightSaveRequest) {
		Flight flight = flightRepository.save(
			Flight.create(
				flightSaveRequest.name(),
				flightSaveRequest.description(),
				flightSaveRequest.price(),
				flightSaveRequest.quantity(),
				flightSaveRequest.airline(),
				flightSaveRequest.flightNumber(),
				flightSaveRequest.departureAirport(),
				flightSaveRequest.arrivalAirport(),
				flightSaveRequest.departureTime(),
				flightSaveRequest.arrivalTime()
			)
		);

		return new FlightResponse(flight);
	}

	@Transactional
	public FlightResponse updateFlight(Long id, FlightSaveRequest flightSaveRequest) {
		Flight flight = flightRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, id));

		flight.update(
			flightSaveRequest.name(),
			flightSaveRequest.description(),
			flightSaveRequest.price(),
			flightSaveRequest.quantity(),
			flightSaveRequest.airline(),
			flightSaveRequest.flightNumber(),
			flightSaveRequest.departureAirport(),
			flightSaveRequest.arrivalAirport(),
			flightSaveRequest.departureTime(),
			flightSaveRequest.arrivalTime()
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
