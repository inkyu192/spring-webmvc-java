package spring.webmvc.domain.model.vo;

public record ProductExposureAttribute(
	boolean isPromotional,
	boolean isNewArrival,
	boolean isFeatured,
	boolean isLowStock
) {
}
