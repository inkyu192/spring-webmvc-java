package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record CurationProductResponse(
	Long id,
	String name,
	String description,
	Long price,
	ProductExposureAttribute exposureAttribute
) {
	public static CurationProductResponse of(CurationProductResult result) {
		return new CurationProductResponse(
			result.id(),
			result.name(),
			result.description(),
			result.price(),
			result.exposureAttribute()
		);
	}
}
