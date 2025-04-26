package spring.webmvc.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.FlightService;
import spring.webmvc.presentation.dto.request.FlightCreateRequest;
import spring.webmvc.presentation.dto.request.FlightUpdateRequest;
import spring.webmvc.presentation.dto.response.FlightResponse;

@RestController
@RequestMapping("/products/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightService flightService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_READER')")
	public FlightResponse findFlight(@PathVariable Long id) {
		return new FlightResponse(flightService.findFlight(id));
	}

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public FlightResponse createFlight(
		@RequestBody @Validated FlightCreateRequest flightCreateRequest
	) {
		return new FlightResponse(
			flightService.createFlight(
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
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	public FlightResponse updateFlight(
		@PathVariable Long id,
		@RequestBody @Validated FlightUpdateRequest flightUpdateRequest
	) {
		return new FlightResponse(
			flightService.updateFlight(
				id,
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
			)
		);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PRODUCT_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFlight(@PathVariable Long id) {
		flightService.deleteFlight(id);
	}
}
