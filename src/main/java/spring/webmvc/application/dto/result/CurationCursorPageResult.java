package spring.webmvc.application.dto.result;

import java.util.List;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationCursorPageResult(
	Long id,
	String title,
	CurationCategory category,
	CursorPage<CurationProductResult> productPage
) {

	public static CurationCursorPageResult of(
		Curation curation,
		CursorPage<CurationProduct> page
	) {
		return new CurationCursorPageResult(
			curation.getId(),
			curation.getTitle(),
			curation.getCategory(),
			page.map(CurationProductResult::of)
		);
	}

	public static CurationCursorPageResult of(
		Curation curation,
		List<Product> products
	) {
		return new CurationCursorPageResult(
			curation.getId(),
			curation.getTitle(),
			curation.getCategory(),
			new CursorPage<>(
				products.stream()
					.map(CurationProductResult::of)
					.toList(),
				(long)products.size(),
				false,
				null
			)
		);
	}
}
