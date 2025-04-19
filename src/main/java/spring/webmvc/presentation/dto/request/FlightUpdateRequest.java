package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlightUpdateRequest(
    @NotBlank
    String name,
    @NotBlank
    String description,
    @Min(100)
    int price,
    @Max(9999)
    int quantity,
    @NotBlank
    String airline,
    @NotBlank
    String flightNumber,
    @NotBlank
    String departureAirport,
    @NotBlank
    String arrivalAirport,
    @NotNull
    Instant departureTime,
    @NotNull
    Instant arrivalTime
) {
}