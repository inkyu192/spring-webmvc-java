package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationProductResult;

public record CurationProductResponse(
	Long id,
	String name,
	String description,
	Long price
) {
	public static CurationProductResponse of(CurationProductResult result) {
		return new CurationProductResponse(
			result.id(),
			result.name(),
			result.description(),
			result.price()
		);
	}
}
