package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CurationProductResult;

public record CurationProductResponse(
	Long id,
	String title,
	CursorPageResponse page,
	List<ProductResponse> products
) {
	public CurationProductResponse(CurationProductResult result) {
		this(
			result.curation().id(),
			result.curation().title(),
			new CursorPageResponse(result.productPage()),
			result.productPage().content().stream().map(ProductResponse::new).toList()
		);
	}
}
