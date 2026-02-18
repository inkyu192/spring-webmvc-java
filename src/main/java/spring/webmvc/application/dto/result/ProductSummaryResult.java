package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductSummaryResult(
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
	public static ProductSummaryResult of(Product product) {
		return new ProductSummaryResult(
			product.getId(),
			product.getCategory(),
			product.getStatus(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			product.getExposureAttribute(),
			product.getCreatedAt()
		);
	}
}
