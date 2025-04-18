package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Flight;

public record FlightResponse(
    Long id,
    String name,
    String description,
    int price,
    int quantity,
    Instant createdAt,
    String airline,
    String flightNumber,
    String departureAirport,
    String arrivalAirport,
    Instant departureTime,
    Instant arrivalTime
) {
    public FlightResponse(Flight flight) {
        this(
            flight.getId(),
            flight.getProduct().getName(),
            flight.getProduct().getDescription(),
            flight.getProduct().getPrice(),
            flight.getProduct().getQuantity(),
            flight.getProduct().getCreatedAt(),
            flight.getAirline(),
            flight.getFlightNumber(),
            flight.getDepartureAirport(),
            flight.getArrivalAirport(),
            flight.getDepartureTime(),
            flight.getArrivalTime()
        );
    }
}
