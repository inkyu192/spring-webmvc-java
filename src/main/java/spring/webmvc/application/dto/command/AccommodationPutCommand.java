package spring.webmvc.application.dto.command;

import java.time.Instant;

public record AccommodationPutCommand(
	String place,
	Instant checkInTime,
	Instant checkOutTime
) implements ProductAttributePutCommand {
}
