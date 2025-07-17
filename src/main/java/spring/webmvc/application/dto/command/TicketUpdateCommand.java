package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class TicketUpdateCommand extends ProductUpdateCommand {
	private final String place;
	private final Instant performanceTime;
	private final String duration;
	private final String ageLimit;

	public TicketUpdateCommand(
		Category category,
		String name,
		String description,
		long price,
		long quantity,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		super(category, name, description, price, quantity);
		this.place = place;
		this.performanceTime = performanceTime;
		this.duration = duration;
		this.ageLimit = ageLimit;
	}
}
