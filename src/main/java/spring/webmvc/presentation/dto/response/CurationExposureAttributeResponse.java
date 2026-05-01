package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationExposureAttributeResult;
import spring.webmvc.domain.model.enums.CurationLayout;

public record CurationExposureAttributeResponse(CurationLayout layout) {

	public static CurationExposureAttributeResponse of(CurationExposureAttributeResult result) {
		return new CurationExposureAttributeResponse(result.layout());
	}
}
