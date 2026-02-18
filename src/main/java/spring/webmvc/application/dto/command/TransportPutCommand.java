package spring.webmvc.application.dto.command;

import java.time.Instant;

public record TransportPutCommand(
	String departureLocation,
	String arrivalLocation,
	Instant departureTime,
	Instant arrivalTime
) implements ProductAttributePutCommand {
}
