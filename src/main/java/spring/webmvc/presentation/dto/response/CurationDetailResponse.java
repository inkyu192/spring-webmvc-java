package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.vo.CurationAttribute;

public record CurationDetailResponse(
	Long id,
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationAttribute attribute,
	CurationExposureAttributeResponse exposureAttribute,
	List<CurationProductResponse> products
) {
	public static CurationDetailResponse of(CurationDetailResult result) {
		return new CurationDetailResponse(
			result.id(),
			result.title(),
			result.placement(),
			result.type(),
			result.attribute(),
			CurationExposureAttributeResponse.of(result.exposureAttribute()),
			result.products().stream()
				.map(CurationProductResponse::of)
				.toList()
		);
	}
}
