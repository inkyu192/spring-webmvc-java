package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.domain.model.enums.CurationCategory;

public record CurationDetailResponse(
	Long id,
	String title,
	CurationCategory category,
	List<CurationProductResponse> products
) {
	public static CurationDetailResponse of(CurationDetailResult result) {
		return new CurationDetailResponse(
			result.id(),
			result.title(),
			result.category(),
			result.products().stream()
				.map(CurationProductResponse::of)
				.toList()
		);
	}
}
