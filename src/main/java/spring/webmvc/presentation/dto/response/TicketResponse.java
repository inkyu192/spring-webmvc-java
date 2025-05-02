package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.TicketDto;
import spring.webmvc.domain.model.entity.Ticket;

public record TicketResponse(
    Long id,
    String name,
    String description,
    int price,
    int quantity,
    Instant createdAt,
    String place,
    Instant performanceTime,
    String duration,
    String ageLimit
) {
    public TicketResponse(Ticket ticket) {
        this(
            ticket.getId(),
            ticket.getProduct().getName(),
            ticket.getProduct().getDescription(),
            ticket.getProduct().getPrice(),
            ticket.getProduct().getQuantity(),
            ticket.getProduct().getCreatedAt(),
            ticket.getPlace(),
            ticket.getPerformanceTime(),
            ticket.getDuration(),
            ticket.getAgeLimit()
        );
    }

    public TicketResponse(TicketDto ticketDto) {
        this(
            ticketDto.id(),
            ticketDto.name(),
            ticketDto.description(),
            ticketDto.price(),
            ticketDto.quantity(),
            ticketDto.createdAt(),
            ticketDto.place(),
            ticketDto.performanceTime(),
            ticketDto.duration(),
            ticketDto.ageLimit()
        );
    }
}
