package spring.webmvc.application.dto.result;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class AccommodationResult extends ProductResult {
	private final Long accommodationId;
	private final String place;
	private final Instant checkInTime;
	private final Instant checkOutTime;

	@JsonCreator
	public AccommodationResult(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		Instant createdAt,
		Long accommodationId,
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		super(id, Category.ACCOMMODATION, name, description, price, quantity, createdAt);
		this.accommodationId = accommodationId;
		this.place = place;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}

	public AccommodationResult(Accommodation accommodation) {
		super(accommodation.getProduct());
		accommodationId = accommodation.getId();
		place = accommodation.getPlace();
		checkInTime = accommodation.getCheckInTime();
		checkOutTime = accommodation.getCheckOutTime();
	}
}