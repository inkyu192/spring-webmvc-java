package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductExposureAttributeResult(
	boolean isPromotional,
	boolean isNewArrival,
	boolean isFeatured,
	boolean isLowStock,
	boolean isRecommended,
	boolean isPersonalPick
) {
	public static ProductExposureAttributeResult of(
		ProductExposureAttribute vo,
		UserProductBadge badge
	) {
		return new ProductExposureAttributeResult(
			vo.isPromotional(),
			vo.isNewArrival(),
			vo.isFeatured(),
			vo.isLowStock(),
			badge != null && Boolean.TRUE.equals(badge.getIsRecommended()),
			badge != null && Boolean.TRUE.equals(badge.getIsPersonalPick())
		);
	}

	public static ProductExposureAttributeResult of(ProductExposureAttribute vo) {
		return of(vo, null);
	}
}
