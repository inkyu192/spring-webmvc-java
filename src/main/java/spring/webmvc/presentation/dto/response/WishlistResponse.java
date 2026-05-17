package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.WishlistResult;

public record WishlistResponse(
	Long id,
	ProductSummaryResponse product,
	Instant createdAt
) {
	public static WishlistResponse of(WishlistResult result) {
		return new WishlistResponse(
			result.id(),
			ProductSummaryResponse.of(result.product()),
			result.createdAt()
		);
	}
}
