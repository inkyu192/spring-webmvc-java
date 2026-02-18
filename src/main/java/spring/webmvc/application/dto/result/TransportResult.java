package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Transport;

public record TransportResult(
	String departureLocation,
	String arrivalLocation,
	Instant departureTime,
	Instant arrivalTime
) implements ProductAttributeResult {
	public static TransportResult of(Transport transport) {
		return new TransportResult(
			transport.getDepartureLocation(),
			transport.getArrivalLocation(),
			transport.getDepartureTime(),
			transport.getArrivalTime()
		);
	}
}
