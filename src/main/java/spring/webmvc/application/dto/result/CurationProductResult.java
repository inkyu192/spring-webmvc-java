package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.cache.CurationProductCache;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public record CurationProductResult(
	CurationResult curation,
	CursorPage<ProductResult> productPage
) {
	public CurationProductResult(Curation curation, CursorPage<Product> productPage) {
		this(
			new CurationResult(curation),
			productPage.map(ProductResult::new)
		);
	}

	public CurationProductResult(CurationProductCache curationProduct) {
		this(
			new CurationResult(curationProduct.curation()),
			curationProduct.productPage().map(ProductResult::new)
		);
	}
}
