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
import spring.webmvc.presentation.dto.request.FlightCreateRequest;
import spring.webmvc.presentation.dto.request.FlightUpdateRequest;
import spring.webmvc.presentation.dto.response.FlightResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Test
    @DisplayName("createFlight: flight 저장 후 반환한다")
    void createFlight() {
        // Given
        FlightCreateRequest request = new FlightCreateRequest(
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

        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(flight);

        // When
        FlightResponse response = flightService.createFlight(request);

        // Then
        Assertions.assertThat(request.name()).isEqualTo(response.name());
        Assertions.assertThat(request.description()).isEqualTo(response.description());
        Assertions.assertThat(request.price()).isEqualTo(response.price());
        Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
        Assertions.assertThat(request.airline()).isEqualTo(response.airline());
        Assertions.assertThat(request.flightNumber()).isEqualTo(response.flightNumber());
        Assertions.assertThat(request.departureAirport()).isEqualTo(response.departureAirport());
        Assertions.assertThat(request.arrivalAirport()).isEqualTo(response.arrivalAirport());
    }

    @Test
    @DisplayName("findFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
    void findFlightCase1() {
        // Given
        Long flightId = 1L;

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> flightService.findFlight(flightId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findFlight: Flight 있을 경우 조회 후 반환한다")
    void findFlightCase2() {
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
        FlightResponse response = flightService.findFlight(flightId);

        // Then
        Assertions.assertThat(flight.getProduct().getName()).isEqualTo(response.name());
        Assertions.assertThat(flight.getProduct().getDescription()).isEqualTo(response.description());
        Assertions.assertThat(flight.getProduct().getPrice()).isEqualTo(response.price());
        Assertions.assertThat(flight.getProduct().getQuantity()).isEqualTo(response.quantity());
        Assertions.assertThat(flight.getAirline()).isEqualTo(response.airline());
        Assertions.assertThat(flight.getFlightNumber()).isEqualTo(response.flightNumber());
        Assertions.assertThat(flight.getDepartureAirport()).isEqualTo(response.departureAirport());
        Assertions.assertThat(flight.getArrivalAirport()).isEqualTo(response.arrivalAirport());
    }

    @Test
    @DisplayName("updateFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
    void updateFlightCase1() {
        // Given
        Long flightId = 1L;
        FlightUpdateRequest request = new FlightUpdateRequest(
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

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> flightService.updateFlight(flightId, request))
            .isInstanceOf(EntityNotFoundException.class);
    }
    
    @Test
    @DisplayName("updateFlight: Flight 있을 경우 수정 후 반환한다")
    void updateFlightCase2() {
        // Given
        Long flightId = 1L;
        FlightUpdateRequest request = new FlightUpdateRequest(
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
        FlightResponse response = flightService.updateFlight(flightId, request);

        // Then
        Assertions.assertThat(request.name()).isEqualTo(response.name());
        Assertions.assertThat(request.description()).isEqualTo(response.description());
        Assertions.assertThat(request.price()).isEqualTo(response.price());
        Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
        Assertions.assertThat(request.airline()).isEqualTo(response.airline());
        Assertions.assertThat(request.flightNumber()).isEqualTo(response.flightNumber());
        Assertions.assertThat(request.departureAirport()).isEqualTo(response.departureAirport());
        Assertions.assertThat(request.arrivalAirport()).isEqualTo(response.arrivalAirport());

        Mockito.verify(flightRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("deleteFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
    void deleteFlightCase1() {
        // Given
        Long flightId = 1L;

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> flightService.deleteFlight(flightId)).isInstanceOf(EntityNotFoundException.class);
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