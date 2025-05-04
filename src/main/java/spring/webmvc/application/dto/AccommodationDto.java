package spring.webmvc.application.dto;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Accommodation;

public record AccommodationDto(
    Long id,
    String name,
    String description,
    int price,
    int quantity,
    Instant createdAt,
    String place,
    Instant checkInTime,
    Instant checkOutTime
) {
    public AccommodationDto(Accommodation accommodation) {
        this(
            accommodation.getId(),
            accommodation.getProduct().getName(),
            accommodation.getProduct().getDescription(),
            accommodation.getProduct().getPrice(),
            accommodation.getProduct().getQuantity(),
            accommodation.getProduct().getCreatedAt(),
            accommodation.getPlace(),
            accommodation.getCheckInTime(),
            accommodation.getCheckOutTime()
        );
    }
}