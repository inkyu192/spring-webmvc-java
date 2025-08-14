package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public record ProductPageResponse(
	CursorPageResponse page,
	List<ProductResponse> products
) {
	public ProductPageResponse(CursorPage<Product> page) {
		this(
			new CursorPageResponse(page),
			page.content().stream().map(ProductResponse::new).toList()
		);
	}
}
