package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.presentation.dto.request.FlightCreateRequest;

@Getter
public class FlightCreateCommand extends ProductCreateCommand {
	private final String airline;
	private final String flightNumber;
	private final String departureAirport;
	private final String arrivalAirport;
	private final Instant departureTime;
	private final Instant arrivalTime;

	public FlightCreateCommand(FlightCreateRequest flightCreateRequest) {
		super(
			flightCreateRequest.getCategory(),
			flightCreateRequest.getName(),
			flightCreateRequest.getDescription(),
			flightCreateRequest.getPrice(),
			flightCreateRequest.getQuantity()
		);
		airline = flightCreateRequest.getAirline();
		flightNumber = flightCreateRequest.getFlightNumber();
		departureAirport = flightCreateRequest.getDepartureAirport();
		arrivalAirport = flightCreateRequest.getArrivalAirport();
		departureTime = flightCreateRequest.getDepartureTime();
		arrivalTime = flightCreateRequest.getArrivalTime();
	}
}
