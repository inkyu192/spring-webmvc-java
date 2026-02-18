package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationSummaryResponse(
	Long id,
	String title,
	CurationCategory category
) {
	public static CurationSummaryResponse of(CurationSummaryResult result) {
		return new CurationSummaryResponse(
			result.id(),
			result.title(),
			result.category()
		);
	}
}
