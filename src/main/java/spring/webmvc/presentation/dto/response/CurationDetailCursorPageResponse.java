package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationDetailCursorPageResponse(
	Long id,
	String title,
	CurationCategory category,
	CursorPageResponse<CurationProductResponse> products
) {
	public static CurationDetailCursorPageResponse of(CurationCursorPageResult result) {
		return new CurationDetailCursorPageResponse(
			result.id(),
			result.title(),
			result.category(),
			CursorPageResponse.of(
				result.productPage(),
				CurationProductResponse::of
			)
		);
	}
}
