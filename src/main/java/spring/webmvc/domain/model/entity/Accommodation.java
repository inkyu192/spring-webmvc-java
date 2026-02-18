package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String place;
	private Instant checkInTime;
	private Instant checkOutTime;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	public static Accommodation create(
		Product product,
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		Accommodation accommodation = new Accommodation();

		accommodation.product = product;
		accommodation.place = place;
		accommodation.checkInTime = checkInTime;
		accommodation.checkOutTime = checkOutTime;

		return accommodation;
	}

	public void update(
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		this.place = place;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}
}
