package spring.webmvc.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

	@InjectMocks
	private FlightService flightService;

	@Mock
	private FlightRepository flightRepository;

	@Test
	@DisplayName("updateFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
	void updateFlightCase1() {
		// Given
		Long flightId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String airline = "airline";
		String flightNumber = "flightNumber";
		String departureAirport = "departureAirport";
		String arrivalAirport = "arrivalAirport";
		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plus(1, ChronoUnit.HOURS);

		Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() ->
			flightService.updateFlight(
				flightId,
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
		).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateFlight: Flight 있을 경우 수정 후 반환한다")
	void updateFlightCase2() {
		// Given
		Long flightId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String airline = "airline";
		String flightNumber = "flightNumber";
		String departureAirport = "departureAirport";
		String arrivalAirport = "arrivalAirport";
		Instant departureTime = Instant.now();
		Instant arrivalTime = Instant.now().plus(1, ChronoUnit.HOURS);

		Flight flight = Flight.create(
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

		Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

		// When
		Flight result = flightService.updateFlight(
			flightId,
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

		// Then
		Assertions.assertThat(result.getProduct().getName()).isEqualTo(name);
		Assertions.assertThat(result.getProduct().getDescription()).isEqualTo(description);
		Assertions.assertThat(result.getProduct().getPrice()).isEqualTo(price);
		Assertions.assertThat(result.getProduct().getQuantity()).isEqualTo(quantity);
		Assertions.assertThat(result.getAirline()).isEqualTo(airline);
		Assertions.assertThat(result.getFlightNumber()).isEqualTo(flightNumber);
		Assertions.assertThat(result.getDepartureAirport()).isEqualTo(departureAirport);
		Assertions.assertThat(result.getArrivalAirport()).isEqualTo(arrivalAirport);
		Assertions.assertThat(result.getDepartureTime()).isEqualTo(departureTime);
		Assertions.assertThat(result.getArrivalTime()).isEqualTo(arrivalTime);
	}

	@Test
	@DisplayName("deleteFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
	void deleteFlightCase1() {
		// Given
		Long flightId = 1L;

		Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> flightService.deleteFlight(flightId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("deleteFlight: Flight 있을 경우 삭제한다")
	void deleteFlightCase2() {
		// Given
		Long flightId = 1L;
		Flight flight = Flight.create(
			"name",
			"description",
			1000,
			5,
			"airline",
			"flightNumber",
			"departureAirport",
			"arrivalAirport",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.HOURS)
		);

		Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

		// When
		flightService.deleteFlight(flightId);

		// Then
		Mockito.verify(flightRepository, Mockito.times(1)).delete(flight);
	}
}
