package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Wishlist;

public record WishlistResult(
	Long id,
	ProductSummaryResult product,
	Instant createdAt
) {
	public static WishlistResult of(Wishlist wishlist, ProductSummaryResult productResult) {
		return new WishlistResult(
			wishlist.getId(),
			productResult,
			wishlist.getCreatedAt()
		);
	}
}
