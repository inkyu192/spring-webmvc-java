package spring.webmvc.application.dto.result;

import org.springframework.data.domain.Page;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationOffsetPageResult(
	Long id,
	String title,
	CurationCategory category,
	Page<CurationProductResult> productPage
) {

	public static CurationOffsetPageResult of(
		Curation curation,
		Page<CurationProduct> page
	) {
		return new CurationOffsetPageResult(
			curation.getId(),
			curation.getTitle(),
			curation.getCategory(),
			page.map(CurationProductResult::of)
		);
	}
}
