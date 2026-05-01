package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationDetailCursorPageResponse(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResponse exposureAttribute,
	CursorPageResponse<CurationProductResponse> products
) {
	public static CurationDetailCursorPageResponse of(CurationCursorPageResult result) {
		return new CurationDetailCursorPageResponse(
			result.id(),
			result.title(),
			result.placement(),
			result.type(),
			CurationExposureAttributeResponse.of(result.exposureAttribute()),
			CursorPageResponse.of(
				result.productPage(),
				CurationProductResponse::of
			)
		);
	}
}
