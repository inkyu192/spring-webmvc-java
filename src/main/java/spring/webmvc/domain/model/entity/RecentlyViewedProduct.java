package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentlyViewedProduct extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private Instant viewedAt;

	private RecentlyViewedProduct(User user, Product product) {
		this.user = user;
		this.product = product;
		this.viewedAt = Instant.now();
	}

	public static RecentlyViewedProduct create(User user, Product product) {
		return new RecentlyViewedProduct(user, product);
	}

	public void updateViewedAt() {
		this.viewedAt = Instant.now();
	}
}
