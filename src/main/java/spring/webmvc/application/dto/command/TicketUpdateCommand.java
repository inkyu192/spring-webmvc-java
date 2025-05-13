package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.presentation.dto.request.TicketUpdateRequest;

@Getter
public class TicketUpdateCommand extends ProductUpdateCommand {
	private final String place;
	private final Instant performanceTime;
	private final String duration;
	private final String ageLimit;

	public TicketUpdateCommand(TicketUpdateRequest ticketUpdateRequest) {
		super(
			Category.TICKET,
			ticketUpdateRequest.getName(),
			ticketUpdateRequest.getDescription(),
			ticketUpdateRequest.getPrice(),
			ticketUpdateRequest.getQuantity()
		);
		this.place = ticketUpdateRequest.getPlace();
		this.performanceTime = ticketUpdateRequest.getPerformanceTime();
		this.duration = ticketUpdateRequest.getDuration();
		this.ageLimit = ticketUpdateRequest.getAgeLimit();
	}
}
