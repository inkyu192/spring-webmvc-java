package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductDetailResponse(
	Long id,
	ProductCategory category,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductExposureAttributeResponse exposureAttribute,
	Instant createdAt,
	ProductAttributeResponse attribute,
	List<TagResponse> tags
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
			ProductExposureAttributeResponse.of(result.exposureAttribute()),
			result.createdAt(),
			ProductAttributeResponse.of(result.attribute()),
			result.tags().stream().map(TagResponse::of).toList()
		);
	}
}
