package spring.webmvc.application.dto.result;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class TicketResult extends ProductResult {
	private final Long ticketId;
	private final String place;
	private final Instant performanceTime;
	private final String duration;
	private final String ageLimit;

	@JsonCreator
	public TicketResult(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		Instant createdAt,
		Long ticketId,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		super(id, Category.TICKET, name, description, price, quantity, createdAt);
		this.ticketId = ticketId;
		this.place = place;
		this.performanceTime = performanceTime;
		this.duration = duration;
		this.ageLimit = ageLimit;
	}

	public TicketResult(Ticket ticket) {
		super(ticket.getProduct());
		ticketId = ticket.getId();
		place = ticket.getPlace();
		performanceTime = ticket.getPerformanceTime();
		duration = ticket.getDuration();
		ageLimit = ticket.getAgeLimit();
	}
}
