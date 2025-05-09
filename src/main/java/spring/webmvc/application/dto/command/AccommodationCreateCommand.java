package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.presentation.dto.request.AccommodationCreateRequest;

@Getter
public class AccommodationCreateCommand extends ProductCreateCommand {
	private final String place;
	private final Instant checkInTime;
	private final Instant checkOutTime;

	public AccommodationCreateCommand(AccommodationCreateRequest accommodationCreateRequest) {
		super(
			accommodationCreateRequest.getCategory(),
			accommodationCreateRequest.getName(),
			accommodationCreateRequest.getDescription(),
			accommodationCreateRequest.getPrice(),
			accommodationCreateRequest.getQuantity()
		);
		place = accommodationCreateRequest.getPlace();
		checkInTime = accommodationCreateRequest.getCheckInTime();
		checkOutTime = accommodationCreateRequest.getCheckOutTime();
	}
}
