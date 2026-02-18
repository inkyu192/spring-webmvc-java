package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.TransportResult;

public record TransportResponse(
	String departureLocation,
	String arrivalLocation,
	Instant departureTime,
	Instant arrivalTime
) implements ProductAttributeResponse {
	public static TransportResponse of(TransportResult result) {
		return new TransportResponse(
			result.departureLocation(),
			result.arrivalLocation(),
			result.departureTime(),
			result.arrivalTime()
		);
	}
}
