package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.domain.model.entity.Accommodation;

@Getter
public class AccommodationResponse extends ProductResponse {
	private final Long accommodationId;
	private final String place;
	private final Instant checkInTime;
	private final Instant checkOutTime;

	public AccommodationResponse(AccommodationResult accommodationResult) {
		super(accommodationResult);
		accommodationId = accommodationResult.getId();
		place = accommodationResult.getPlace();
		checkInTime = accommodationResult.getCheckInTime();
		checkOutTime = accommodationResult.getCheckOutTime();
	}

	public AccommodationResponse(Accommodation accommodation) {
		super(accommodation.getProduct());
		accommodationId = accommodation.getId();
		place = accommodation.getPlace();
		checkInTime = accommodation.getCheckInTime();
		checkOutTime = accommodation.getCheckOutTime();
	}
}
