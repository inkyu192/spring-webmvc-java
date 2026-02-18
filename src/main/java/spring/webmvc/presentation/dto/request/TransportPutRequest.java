package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import spring.webmvc.application.dto.command.TransportPutCommand;

public record TransportPutRequest(
	String departureLocation,
	String arrivalLocation,
	Instant departureTime,
	Instant arrivalTime
) implements ProductAttributePutRequest {

	@Override
	public TransportPutCommand toCommand() {
		return new TransportPutCommand(departureLocation, arrivalLocation, departureTime, arrivalTime);
	}
}
