package spring.webmvc.presentation.dto.request;

import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductExposureAttributeRequest(
	boolean isPromotional,
	boolean isNewArrival,
	boolean isFeatured,
	boolean isLowStock
) {
	public ProductExposureAttribute toVO() {
		return new ProductExposureAttribute(
			isPromotional,
			isNewArrival,
			isFeatured,
			isLowStock
		);
	}
}
