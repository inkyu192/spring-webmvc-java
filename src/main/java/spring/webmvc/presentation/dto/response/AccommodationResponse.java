package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.AccommodationResult;

public record AccommodationResponse(
	String place,
	Instant checkInTime,
	Instant checkOutTime
) implements ProductAttributeResponse {
	public static AccommodationResponse of(AccommodationResult result) {
		return new AccommodationResponse(
			result.place(),
			result.checkInTime(),
			result.checkOutTime()
		);
	}
}
