package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Accommodation;

public record AccommodationResult(
	String place,
	Instant checkInTime,
	Instant checkOutTime
) implements ProductAttributeResult {
	public static AccommodationResult of(Accommodation accommodation) {
		return new AccommodationResult(
			accommodation.getPlace(),
			accommodation.getCheckInTime(),
			accommodation.getCheckOutTime()
		);
	}
}
