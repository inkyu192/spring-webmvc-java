package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation {

	@Id
	@GeneratedValue
	private Long id;
	private String place;
	private Instant checkInTime;
	private Instant checkOutTime;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id")
	private Product product;

	public static Accommodation create(
		String name,
		String description,
		long price,
		long quantity,
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		Accommodation accommodation = new Accommodation();

		accommodation.product = Product.create(name, description, price, quantity, Category.ACCOMMODATION);
		accommodation.place = place;
		accommodation.checkInTime = checkInTime;
		accommodation.checkOutTime = checkOutTime;

		return accommodation;
	}

	public void update(
		String name,
		String description,
		long price,
		long quantity,
		String place,
		Instant checkInTime,
		Instant checkOutTime
	) {
		this.product.update(name, description, price, quantity);
		this.place = place;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}
}
