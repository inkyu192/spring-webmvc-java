package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class AccommodationUpdateRequest extends ProductUpdateRequest {
	@NotBlank
	private final String place;
	@NotNull
	private final Instant checkInTime;
	@NotNull
	private final Instant checkOutTime;

	public AccommodationUpdateRequest(
		Category category,
		String name,
		String description,
		int price,
		int quantity,
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