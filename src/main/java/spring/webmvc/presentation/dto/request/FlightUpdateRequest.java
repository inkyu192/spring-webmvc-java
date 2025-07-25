package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.webmvc.application.dto.command.FlightUpdateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class FlightUpdateRequest extends ProductUpdateRequest {
	@NotBlank
	private final String airline;
	@NotBlank
	private final String flightNumber;
	@NotBlank
	private final String departureAirport;
	@NotBlank
	private final String arrivalAirport;
	@NotNull
	private final Instant departureTime;
	@NotNull
	private final Instant arrivalTime;

	public FlightUpdateRequest(
		Category category,
		String name,
		String description,
		int price,
		int quantity,
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

	@Override
	public ProductUpdateCommand toCommand() {
		return new FlightUpdateCommand(
			category,
			name,
			description,
			price,
			quantity,
			airline,
			flightNumber,
			departureAirport,
			arrivalAirport,
			departureTime,
			arrivalTime
		);
	}
}