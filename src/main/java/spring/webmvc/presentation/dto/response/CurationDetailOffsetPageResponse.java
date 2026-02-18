package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationDetailOffsetPageResponse(
	Long id,
	String title,
	CurationCategory category,
	OffsetPageResponse<CurationProductResponse> products
) {
	public static CurationDetailOffsetPageResponse of(CurationOffsetPageResult result) {
		return new CurationDetailOffsetPageResponse(
			result.id(),
			result.title(),
			result.category(),
			OffsetPageResponse.of(
				result.productPage(),
				CurationProductResponse::of
			)
		);
	}
}
