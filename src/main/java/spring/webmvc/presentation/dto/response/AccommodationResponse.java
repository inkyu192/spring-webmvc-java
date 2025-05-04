package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.AccommodationDto;
import spring.webmvc.domain.model.entity.Accommodation;

public record AccommodationResponse(
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
	public AccommodationResponse(Accommodation accommodation) {
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

	public AccommodationResponse(AccommodationDto accommodationDto) {
		this(
			accommodationDto.id(),
			accommodationDto.name(),
			accommodationDto.description(),
			accommodationDto.price(),
			accommodationDto.quantity(),
			accommodationDto.createdAt(),
			accommodationDto.place(),
			accommodationDto.checkInTime(),
			accommodationDto.checkOutTime()
		);
	}
}
