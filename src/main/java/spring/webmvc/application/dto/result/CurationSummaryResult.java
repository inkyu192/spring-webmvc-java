package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationSummaryResult(
	Long id,
	String title,
	CurationCategory category
) {
	public static CurationSummaryResult of(Curation curation) {
		return new CurationSummaryResult(
			curation.getId(),
			curation.getTitle(),
			curation.getCategory()
		);
	}
}
