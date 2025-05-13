package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.presentation.dto.request.FlightUpdateRequest;

@Getter
public class FlightUpdateCommand extends ProductUpdateCommand {
	private final String airline;
	private final String flightNumber;
	private final String departureAirport;
	private final String arrivalAirport;
	private final Instant departureTime;
	private final Instant arrivalTime;

	public FlightUpdateCommand(FlightUpdateRequest flightUpdateRequest) {
		super(
			flightUpdateRequest.getCategory(),
			flightUpdateRequest.getName(),
			flightUpdateRequest.getDescription(),
			flightUpdateRequest.getPrice(),
			flightUpdateRequest.getQuantity()
		);
		airline = flightUpdateRequest.getAirline();
		flightNumber = flightUpdateRequest.getFlightNumber();
		departureAirport = flightUpdateRequest.getDepartureAirport();
		arrivalAirport = flightUpdateRequest.getArrivalAirport();
		departureTime = flightUpdateRequest.getDepartureTime();
		arrivalTime = flightUpdateRequest.getArrivalTime();
	}
}
