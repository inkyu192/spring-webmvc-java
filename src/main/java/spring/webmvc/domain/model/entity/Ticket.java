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
public class Ticket extends Product {

    private String eventName;
    private String venue;
    private LocalDateTime eventDateTime;
    private String seatNumber;
    private String ticketType; // VIP, standard, etc.

    public static Ticket create(String name, String description, int price, int quantity, 
                               String eventName, String venue, LocalDateTime eventDateTime, 
                               String seatNumber, String ticketType) {
        Ticket ticket = new Ticket();

        ticket.name = name;
        ticket.description = description;
        ticket.price = price;
        ticket.quantity = quantity;
        ticket.category = Category.TICKET;
        ticket.eventName = eventName;
        ticket.venue = venue;
        ticket.eventDateTime = eventDateTime;
        ticket.seatNumber = seatNumber;
        ticket.ticketType = ticketType;

        return ticket;
    }

    public void update(String name, String description, int price, int quantity,
                      String eventName, String venue, LocalDateTime eventDateTime, 
                      String seatNumber, String ticketType) {
        super.update(name, description, price, quantity, Category.TICKET);
        this.eventName = eventName;
        this.venue = venue;
        this.eventDateTime = eventDateTime;
        this.seatNumber = seatNumber;
        this.ticketType = ticketType;
    }
}
