package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationSummaryResponse(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResponse exposureAttribute
) {
	public static CurationSummaryResponse of(CurationSummaryResult result) {
		return new CurationSummaryResponse(
			result.id(),
			result.title(),
			result.placement(),
			result.type(),
			CurationExposureAttributeResponse.of(result.exposureAttribute())
		);
	}
}
