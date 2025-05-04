package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.domain.model.entity.Flight;

@Getter
public class FlightResponse extends ProductResponse {
	private final Long flightId;
	private final String airline;
	private final String flightNumber;
	private final String departureAirport;
	private final String arrivalAirport;
	private final Instant departureTime;
	private final Instant arrivalTime;

	public FlightResponse(FlightResult flightResult) {
		super(flightResult);
		flightId = flightResult.getId();
		airline = flightResult.getAirline();
		flightNumber = flightResult.getFlightNumber();
		departureAirport = flightResult.getDepartureAirport();
		arrivalAirport = flightResult.getArrivalAirport();
		departureTime = flightResult.getDepartureTime();
		arrivalTime = flightResult.getArrivalTime();
	}

	public FlightResponse(Flight flight) {
		super(flight.getProduct());
		flightId = flight.getId();
		airline = flight.getAirline();
		flightNumber = flight.getFlightNumber();
		departureAirport = flight.getDepartureAirport();
		arrivalAirport = flight.getArrivalAirport();
		departureTime = flight.getDepartureTime();
		arrivalTime = flight.getArrivalTime();
	}
}
