package spring.webmvc.application.dto.result;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductSummaryResult(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttributeResult exposureAttribute,
	Instant createdAt
) {
	public static ProductSummaryResult of(Product product) {
		return of(product, null, false, false);
	}

	public static ProductSummaryResult of(Product product, UserProductBadge badge) {
		return of(product, badge, false, false);
	}

	public static ProductSummaryResult of(Product product, UserProductBadge badge, boolean isRecentlyViewed) {
		return of(product, badge, isRecentlyViewed, false);
	}

	public static ProductSummaryResult of(
		Product product,
		UserProductBadge badge,
		boolean isRecentlyViewed,
		boolean isWished
	) {
		return new ProductSummaryResult(
			product.getId(),
			product.getCategory(),
			product.getStatus(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			ProductExposureAttributeResult.of(product.getExposureAttribute(), badge, isRecentlyViewed, isWished),
			product.getCreatedAt()
		);
	}
}
