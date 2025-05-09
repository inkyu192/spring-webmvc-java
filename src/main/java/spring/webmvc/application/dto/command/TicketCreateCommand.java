package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.presentation.dto.request.TicketCreateRequest;

@Getter
public class TicketCreateCommand extends ProductCreateCommand {
	private final String place;
	private final Instant performanceTime;
	private final String duration;
	private final String ageLimit;

	public TicketCreateCommand(TicketCreateRequest ticketCreateRequest) {
		super(
			ticketCreateRequest.getCategory(),
			ticketCreateRequest.getName(),
			ticketCreateRequest.getDescription(),
			ticketCreateRequest.getPrice(),
			ticketCreateRequest.getQuantity()
		);
		place = ticketCreateRequest.getPlace();
		performanceTime = ticketCreateRequest.getPerformanceTime();
		duration = ticketCreateRequest.getDuration();
		ageLimit = ticketCreateRequest.getAgeLimit();
	}
}
