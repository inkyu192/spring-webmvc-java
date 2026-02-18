package spring.webmvc.application.dto.result;

import java.util.List;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationDetailResult(
	Long id,
	String title,
	CurationCategory category,
	List<CurationProductResult> products
) {
	public static CurationDetailResult of(Curation curation) {
		List<CurationProductResult> products = curation.getCurationProducts().stream()
			.map(CurationProductResult::of)
			.toList();

		return new CurationDetailResult(
			curation.getId(),
			curation.getTitle(),
			curation.getCategory(),
			products
		);
	}
}
