package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import spring.webmvc.application.dto.command.AccommodationPutCommand;

public record AccommodationPutRequest(
	String place,
	Instant checkInTime,
	Instant checkOutTime
) implements ProductAttributePutRequest {

	@Override
	public AccommodationPutCommand toCommand() {
		return new AccommodationPutCommand(place, checkInTime, checkOutTime);
	}
}
