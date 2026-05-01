package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.UserProductBadge;

public record CurationProductResult(
	Long id,
	String name,
	String description,
	Long price,
	ProductExposureAttributeResult exposureAttribute
) {
	public static CurationProductResult of(CurationProduct curationProduct) {
		return of(curationProduct, null);
	}

	public static CurationProductResult of(CurationProduct curationProduct, UserProductBadge badge) {
		return new CurationProductResult(
			curationProduct.getProduct().getId(),
			curationProduct.getProduct().getName(),
			curationProduct.getProduct().getDescription(),
			curationProduct.getProduct().getPrice(),
			ProductExposureAttributeResult.of(curationProduct.getProduct().getExposureAttribute(), badge)
		);
	}

	public static CurationProductResult of(Product product) {
		return of(product, null);
	}

	public static CurationProductResult of(Product product, UserProductBadge badge) {
		return new CurationProductResult(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			ProductExposureAttributeResult.of(product.getExposureAttribute(), badge)
		);
	}
}
