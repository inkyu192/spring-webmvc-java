package spring.webmvc.application.dto.result;

import org.springframework.data.domain.Page;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationOffsetPageResult(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationExposureAttributeResult exposureAttribute,
	Page<CurationProductResult> productPage
) {

	public static CurationOffsetPageResult of(
		Curation curation,
		Page<CurationProductResult> productPage
	) {
		return new CurationOffsetPageResult(
			curation.getId(),
			curation.getTitle(),
			curation.getPlacement(),
			curation.getType(),
			CurationExposureAttributeResult.of(curation.getExposureAttribute()),
			productPage
		);
	}
}
