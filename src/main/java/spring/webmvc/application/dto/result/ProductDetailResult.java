package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductDetailResult(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttribute exposureAttribute,
	Instant createdAt,
	ProductAttributeResult attribute
) {
	public static ProductDetailResult of(Product product, ProductAttributeResult attribute) {
		return new ProductDetailResult(
			product.getId(),
			product.getCategory(),
			product.getStatus(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			product.getExposureAttribute(),
			product.getCreatedAt(),
			attribute
		);
	}
}
