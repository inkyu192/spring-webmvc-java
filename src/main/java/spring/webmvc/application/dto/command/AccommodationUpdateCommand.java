package spring.webmvc.application.dto.command;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class AccommodationUpdateCommand extends ProductUpdateCommand {
	private final String place;
	private final Instant checkInTime;
	private final Instant checkOutTime;

	public AccommodationUpdateCommand(
		Category category,
		String name,
		String description,
		long price,
		long quantity,
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		super(category, name, description, price, quantity);
		this.place = place;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}
}
