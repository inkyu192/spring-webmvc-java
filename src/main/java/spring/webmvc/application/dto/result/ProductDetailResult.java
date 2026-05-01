package spring.webmvc.application.dto.result;

import java.time.Instant;
import java.util.List;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.Tag;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductDetailResult(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttributeResult exposureAttribute,
	Instant createdAt,
	ProductAttributeResult attribute,
	List<TagResult> tags
) {
	public static ProductDetailResult of(Product product, ProductAttributeResult attribute) {
		return of(product, attribute, null, List.of());
	}

	public static ProductDetailResult of(Product product, ProductAttributeResult attribute, UserProductBadge badge) {
		return of(product, attribute, badge, List.of());
	}

	public static ProductDetailResult of(
		Product product,
		ProductAttributeResult attribute,
		UserProductBadge badge,
		List<Tag> tags
	) {
		return new ProductDetailResult(
			product.getId(),
			product.getCategory(),
			product.getStatus(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			ProductExposureAttributeResult.of(product.getExposureAttribute(), badge),
			product.getCreatedAt(),
			attribute,
			tags.stream().map(TagResult::of).toList()
		);
	}
}
