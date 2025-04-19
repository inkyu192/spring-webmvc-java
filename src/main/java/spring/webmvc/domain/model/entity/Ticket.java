package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
public class Ticket {

	@Id
	@GeneratedValue
	private Long id;
	private String place;
	private Instant performanceTime;
	private String duration;
	private String ageLimit;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;

	public static Ticket create(
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		Ticket ticket = new Ticket();

		ticket.product = Product.create(name, description, price, quantity, Category.TICKET);
		ticket.place = place;
		ticket.performanceTime = performanceTime;
		ticket.duration = duration;
		ticket.ageLimit = ageLimit;

		return ticket;
	}

	public void update(
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant performanceTime,
		String duration,
		String ageLimit
	) {
		this.product.update(name, description, price, quantity);
		this.place = place;
		this.performanceTime = performanceTime;
		this.duration = duration;
		this.ageLimit = ageLimit;
	}
}
