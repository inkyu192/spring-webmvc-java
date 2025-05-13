package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.presentation.dto.request.AccommodationUpdateRequest;

@Getter
public class AccommodationUpdateCommand extends ProductUpdateCommand {
	private final String place;
	private final Instant checkInTime;
	private final Instant checkOutTime;

	public AccommodationUpdateCommand(AccommodationUpdateRequest accommodationUpdateRequest) {
		super(
			accommodationUpdateRequest.getCategory(),
			accommodationUpdateRequest.getName(),
			accommodationUpdateRequest.getDescription(),
			accommodationUpdateRequest.getPrice(),
			accommodationUpdateRequest.getQuantity()
		);
		place = accommodationUpdateRequest.getPlace();
		checkInTime = accommodationUpdateRequest.getCheckInTime();
		checkOutTime = accommodationUpdateRequest.getCheckOutTime();
	}
}
