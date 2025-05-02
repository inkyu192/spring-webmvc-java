package spring.webmvc.application.dto;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Ticket;

public record TicketDto(
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
	public TicketDto(Ticket ticket) {
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
}
