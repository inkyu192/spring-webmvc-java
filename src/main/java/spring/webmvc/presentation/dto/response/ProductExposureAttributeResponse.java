package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.ProductExposureAttributeResult;

public record ProductExposureAttributeResponse(
	boolean isPromotional,
	boolean isNewArrival,
	boolean isFeatured,
	boolean isLowStock,
	boolean isRecommended,
	boolean isPersonalPick,
	boolean isRecentlyViewed,
	boolean isWished
) {
	public static ProductExposureAttributeResponse of(ProductExposureAttributeResult result) {
		return new ProductExposureAttributeResponse(
			result.isPromotional(),
			result.isNewArrival(),
			result.isFeatured(),
			result.isLowStock(),
			result.isRecommended(),
			result.isPersonalPick(),
			result.isRecentlyViewed(),
			result.isWished()
		);
	}
}
