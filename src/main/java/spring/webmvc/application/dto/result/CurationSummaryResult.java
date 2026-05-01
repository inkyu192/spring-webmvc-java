package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationSummaryResult(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResult exposureAttribute
) {
	public static CurationSummaryResult of(Curation curation) {
		return new CurationSummaryResult(
			curation.getId(),
			curation.getTitle(),
			curation.getPlacement(),
			curation.getType(),
			CurationExposureAttributeResult.of(curation.getExposureAttribute())
		);
	}
}
