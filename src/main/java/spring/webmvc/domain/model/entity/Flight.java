package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flight {

	@Id
	@GeneratedValue
	private Long id;
	private String airline;
	private String flightNumber;
	private String departureAirport;
	private String arrivalAirport;
	private Instant departureTime;
	private Instant arrivalTime;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id")
	private Product product;

	public static Flight create(
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
		Flight flight = new Flight();

		flight.product = Product.create(name, description, price, quantity, Category.FLIGHT);
		flight.airline = airline;
		flight.flightNumber = flightNumber;
		flight.departureAirport = departureAirport;
		flight.arrivalAirport = arrivalAirport;
		flight.departureTime = departureTime;
		flight.arrivalTime = arrivalTime;

		return flight;
	}

	public void update(
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
		this.product.update(name, description, price, quantity);
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.departureAirport = departureAirport;
		this.arrivalAirport = arrivalAirport;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}
}
