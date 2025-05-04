package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.domain.model.entity.Ticket;

@Getter
public class TicketResponse extends ProductResponse {
	private final Long ticketId;
	private final String place;
	private final Instant performanceTime;
	private final String duration;
	private final String ageLimit;

	public TicketResponse(TicketResult ticketResult) {
		super(ticketResult);
		ticketId = ticketResult.getId();
		place = ticketResult.getPlace();
		performanceTime = ticketResult.getPerformanceTime();
		duration = ticketResult.getDuration();
		ageLimit = ticketResult.getAgeLimit();
	}

	public TicketResponse(Ticket ticket) {
		super(ticket.getProduct());
		ticketId = ticket.getId();
		place = ticket.getPlace();
		performanceTime = ticket.getPerformanceTime();
		duration = ticket.getDuration();
		ageLimit = ticket.getAgeLimit();
	}
}
