package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationDetailOffsetPageResponse(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResponse exposureAttribute,
	OffsetPageResponse<CurationProductResponse> products
) {
	public static CurationDetailOffsetPageResponse of(CurationOffsetPageResult result) {
		return new CurationDetailOffsetPageResponse(
			result.id(),
			result.title(),
			result.placement(),
			result.type(),
			CurationExposureAttributeResponse.of(result.exposureAttribute()),
			OffsetPageResponse.of(
				result.productPage(),
				CurationProductResponse::of
			)
		);
	}
}
