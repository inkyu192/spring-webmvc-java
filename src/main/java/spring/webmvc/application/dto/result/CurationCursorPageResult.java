package spring.webmvc.application.dto.result;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationCursorPageResult(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResult exposureAttribute,
	CursorPage<CurationProductResult> productPage
) {

	public static CurationCursorPageResult of(
		Curation curation,
		CursorPage<CurationProductResult> productPage
	) {
		return new CurationCursorPageResult(
			curation.getId(),
			curation.getTitle(),
			curation.getPlacement(),
			curation.getType(),
			CurationExposureAttributeResult.of(curation.getExposureAttribute()),
			productPage
		);
	}
}
