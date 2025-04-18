package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flight extends Product {

    private String airline;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public static Flight create(String name, String description, int price, int quantity, 
                               String airline, String flightNumber, String departureAirport, 
                               String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Flight flight = new Flight();

        flight.name = name;
        flight.description = description;
        flight.price = price;
        flight.quantity = quantity;
        flight.category = Category.FLIGHT;
        flight.airline = airline;
        flight.flightNumber = flightNumber;
        flight.departureAirport = departureAirport;
        flight.arrivalAirport = arrivalAirport;
        flight.departureTime = departureTime;
        flight.arrivalTime = arrivalTime;

        return flight;
    }

    public void update(String name, String description, int price, int quantity,
                      String airline, String flightNumber, String departureAirport, 
                      String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        super.update(name, description, price, quantity, Category.FLIGHT);
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
