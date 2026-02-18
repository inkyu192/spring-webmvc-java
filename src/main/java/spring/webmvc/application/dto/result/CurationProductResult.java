package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;

public record CurationProductResult(
	Long id,
	String name,
	String description,
	Long price
) {
	public static CurationProductResult of(CurationProduct curationProduct) {
		return new CurationProductResult(
			curationProduct.getProduct().getId(),
			curationProduct.getProduct().getName(),
			curationProduct.getProduct().getDescription(),
			curationProduct.getProduct().getPrice()
		);
	}

	public static CurationProductResult of(Product product) {
		return new CurationProductResult(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getPrice()
		);
	}
}
