package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.webmvc.application.dto.command.AccommodationCreateCommand;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class AccommodationCreateRequest extends ProductCreateRequest {
	@NotBlank
	private final String place;
	@NotNull
	private final Instant checkInTime;
	@NotNull
	private final Instant checkOutTime;

	public AccommodationCreateRequest(
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

	@Override
	public AccommodationCreateCommand toCommand() {
		return new AccommodationCreateCommand(
			category,
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);
	}
}
