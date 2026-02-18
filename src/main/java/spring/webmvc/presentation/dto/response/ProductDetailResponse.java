package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductDetailResponse(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttribute exposureAttribute,
	Instant createdAt,
	ProductAttributeResponse attribute
) {
	public static ProductDetailResponse of(ProductDetailResult result) {
		return new ProductDetailResponse(
			result.id(),
			result.category(),
			result.status(),
			result.name(),
			result.description(),
			result.price(),
			result.quantity(),
			result.exposureAttribute(),
			result.createdAt(),
			ProductAttributeResponse.of(result.attribute())
		);
	}
}
