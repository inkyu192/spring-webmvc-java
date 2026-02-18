package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductSummaryResponse(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttribute exposureAttribute,
	Instant createdAt
) {
	public static ProductSummaryResponse of(ProductSummaryResult result) {
		return new ProductSummaryResponse(
			result.id(),
			result.category(),
			result.status(),
			result.name(),
			result.description(),
			result.price(),
			result.quantity(),
			result.exposureAttribute(),
			result.createdAt()
		);
	}
}
