package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductExposureAttributeResult(
	boolean isPromotional,
	boolean isNewArrival,
	boolean isFeatured,
	boolean isLowStock,
	boolean isRecommended,
	boolean isPersonalPick,
	boolean isRecentlyViewed,
	boolean isWished
) {
	public static ProductExposureAttributeResult of(
		ProductExposureAttribute vo,
		UserProductBadge badge,
		boolean isRecentlyViewed,
		boolean isWished
	) {
		return new ProductExposureAttributeResult(
			vo.isPromotional(),
			vo.isNewArrival(),
			vo.isFeatured(),
			vo.isLowStock(),
			badge != null && Boolean.TRUE.equals(badge.getIsRecommended()),
			badge != null && Boolean.TRUE.equals(badge.getIsPersonalPick()),
			isRecentlyViewed,
			isWished
		);
	}

	public static ProductExposureAttributeResult of(
		ProductExposureAttribute vo,
		UserProductBadge badge,
		boolean isRecentlyViewed
	) {
		return of(vo, badge, isRecentlyViewed, false);
	}

	public static ProductExposureAttributeResult of(ProductExposureAttribute vo, UserProductBadge badge) {
		return of(vo, badge, false, false);
	}

	public static ProductExposureAttributeResult of(ProductExposureAttribute vo) {
		return of(vo, null, false, false);
	}
}
