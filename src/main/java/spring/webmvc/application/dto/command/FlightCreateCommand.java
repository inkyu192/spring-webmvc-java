package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class FlightCreateCommand extends ProductCreateCommand {
	private final String airline;
	private final String flightNumber;
	private final String departureAirport;
	private final String arrivalAirport;
	private final Instant departureTime;
	private final Instant arrivalTime;

	public FlightCreateCommand(
		Category category,
		String name,
		String description,
		long price,
		long quantity,
		String airline,
		String flightNumber,
		String departureAirport,
		String arrivalAirport,
		Instant departureTime,
		Instant arrivalTime
	) {
		super(category, name, description, price, quantity);
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}
}
