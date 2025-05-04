package spring.webmvc.application.dto.result;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class FlightResult extends ProductResult {
	private final Long flightId;
	private final String airline;
	private final String flightNumber;
	private final String departureAirport;
	private final String arrivalAirport;
	private final Instant departureTime;
	private final Instant arrivalTime;

	@JsonCreator
	public FlightResult(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		Instant createdAt,
		Long flightId,
		String airline,
		String flightNumber,
		String departureAirport,
		String arrivalAirport,
		Instant departureTime,
		Instant arrivalTime
	) {
		super(id, Category.FLIGHT, name, description, price, quantity, createdAt);
		this.flightId = flightId;
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}

	public FlightResult(Flight flight) {
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