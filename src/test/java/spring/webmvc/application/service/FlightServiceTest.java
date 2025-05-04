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

import spring.webmvc.application.dto.FlightDto;
import spring.webmvc.domain.cache.FlightCache;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightCache flightCache;

    @Mock
    private JsonSupport jsonSupport;

    @Test
    @DisplayName("createFlight: Flight 저장 후 반환한다")
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
    @DisplayName("findFlight: Flight 없을 경우 EntityNotFoundException 발생한다")
    void findFlightCase1() {
        // Given
        Long flightId = 1L;

        Mockito.when(flightCache.get(flightId)).thenReturn(Optional.empty());
        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> flightService.findFlight(flightId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("findFlight: Flight cache 있을 경우 cache 반환한다")
    void findFlightCase2() {
        // Given
        Long flightId = 1L;
        String value = "value";
        FlightDto flightDto = new FlightDto(
            flightId,
            "name",
            "description",
            1000,
            5,
            Instant.now(),
            "airline",
            "flightNumber",
            "departureAirport",
            "arrivalAirport",
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS)
        );

        Mockito.when(flightCache.get(flightId)).thenReturn(Optional.of(value));
        Mockito.when(jsonSupport.readValue(value, FlightDto.class)).thenReturn(Optional.of(flightDto));

        // When
        FlightDto result = flightService.findFlight(flightId);

        // Then
        Assertions.assertThat(result.name()).isEqualTo(flightDto.name());
        Assertions.assertThat(result.description()).isEqualTo(flightDto.description());
        Assertions.assertThat(result.price()).isEqualTo(flightDto.price());
        Assertions.assertThat(result.quantity()).isEqualTo(flightDto.quantity());
        Assertions.assertThat(result.airline()).isEqualTo(flightDto.airline());
        Assertions.assertThat(result.flightNumber()).isEqualTo(flightDto.flightNumber());
        Assertions.assertThat(result.departureAirport()).isEqualTo(flightDto.departureAirport());
        Assertions.assertThat(result.arrivalAirport()).isEqualTo(flightDto.arrivalAirport());
        Assertions.assertThat(result.departureTime()).isEqualTo(flightDto.departureTime());
        Assertions.assertThat(result.arrivalTime()).isEqualTo(flightDto.arrivalTime());
    }

    @Test
    @DisplayName("findFlight: Flight cache 없을 경우 repository 조회 후 반환한다")
    void findFlightCase3() {
        // Given
        Long flightId = 1L;
        String value = "value";
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

        Mockito.when(flightCache.get(flightId)).thenReturn(Optional.empty());
        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        Mockito.when(jsonSupport.writeValueAsString(Mockito.any(FlightDto.class))).thenReturn(Optional.of(value));

        // When
        FlightDto result = flightService.findFlight(flightId);

        // Then
        Assertions.assertThat(result.name()).isEqualTo(flight.getProduct().getName());
        Assertions.assertThat(result.description()).isEqualTo(flight.getProduct().getDescription());
        Assertions.assertThat(result.price()).isEqualTo(flight.getProduct().getPrice());
        Assertions.assertThat(result.quantity()).isEqualTo(flight.getProduct().getQuantity());
        Assertions.assertThat(result.airline()).isEqualTo(flight.getAirline());
        Assertions.assertThat(result.flightNumber()).isEqualTo(flight.getFlightNumber());
        Assertions.assertThat(result.departureAirport()).isEqualTo(flight.getDepartureAirport());
        Assertions.assertThat(result.arrivalAirport()).isEqualTo(flight.getArrivalAirport());
        Assertions.assertThat(result.departureTime()).isEqualTo(flight.getDepartureTime());
        Assertions.assertThat(result.arrivalTime()).isEqualTo(flight.getArrivalTime());
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
