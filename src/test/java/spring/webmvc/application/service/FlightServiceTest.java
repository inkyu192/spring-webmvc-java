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
