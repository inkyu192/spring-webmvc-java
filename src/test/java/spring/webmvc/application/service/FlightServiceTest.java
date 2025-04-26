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
    @DisplayName("createFlight: flight 저장 후 반환한다")
    void createFlight() {
        // Given
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

        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(flight);

        // When
        Flight result = flightService.createFlight(
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
        Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
        Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
        Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
        Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
        Assertions.assertThat(airline).isEqualTo(result.getAirline());
        Assertions.assertThat(flightNumber).isEqualTo(result.getFlightNumber());
        Assertions.assertThat(departureAirport).isEqualTo(result.getDepartureAirport());
        Assertions.assertThat(arrivalAirport).isEqualTo(result.getArrivalAirport());
        Assertions.assertThat(departureTime).isEqualTo(result.getDepartureTime());
        Assertions.assertThat(arrivalTime).isEqualTo(result.getArrivalTime());
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
        Flight result = flightService.findFlight(flightId);

        // Then
        Assertions.assertThat(flight.getProduct().getName()).isEqualTo(result.getProduct().getName());
        Assertions.assertThat(flight.getProduct().getDescription()).isEqualTo(result.getProduct().getDescription());
        Assertions.assertThat(flight.getProduct().getPrice()).isEqualTo(result.getProduct().getPrice());
        Assertions.assertThat(flight.getProduct().getQuantity()).isEqualTo(result.getProduct().getQuantity());
        Assertions.assertThat(flight.getAirline()).isEqualTo(result.getAirline());
        Assertions.assertThat(flight.getFlightNumber()).isEqualTo(result.getFlightNumber());
        Assertions.assertThat(flight.getDepartureAirport()).isEqualTo(result.getDepartureAirport());
        Assertions.assertThat(flight.getArrivalAirport()).isEqualTo(result.getArrivalAirport());
        Assertions.assertThat(flight.getDepartureTime()).isEqualTo(result.getDepartureTime());
        Assertions.assertThat(flight.getArrivalTime()).isEqualTo(result.getArrivalTime());
    }

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
        Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
        Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
        Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
        Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
        Assertions.assertThat(airline).isEqualTo(result.getAirline());
        Assertions.assertThat(flightNumber).isEqualTo(result.getFlightNumber());
        Assertions.assertThat(departureAirport).isEqualTo(result.getDepartureAirport());
        Assertions.assertThat(arrivalAirport).isEqualTo(result.getArrivalAirport());
        Assertions.assertThat(departureTime).isEqualTo(result.getDepartureTime());
        Assertions.assertThat(arrivalTime).isEqualTo(result.getArrivalTime());

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
