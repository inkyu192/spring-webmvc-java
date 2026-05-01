package spring.webmvc.application.dto.result;

import java.util.List;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.vo.CurationAttribute;

public record CurationDetailResult(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationAttribute attribute,
	CurationExposureAttributeResult exposureAttribute,
	List<CurationProductResult> products
) {
	public static CurationDetailResult of(Curation curation) {
		List<CurationProductResult> products = curation.getCurationProducts().stream()
			.map(CurationProductResult::of)
			.toList();

		return new CurationDetailResult(
			curation.getId(),
			curation.getTitle(),
			curation.getPlacement(),
			curation.getType(),
			curation.getAttribute(),
			CurationExposureAttributeResult.of(curation.getExposureAttribute()),
			products
		);
	}
}
