package spring.webmvc.presentation.dto.request;

import spring.webmvc.domain.model.enums.CurationLayout;
import spring.webmvc.domain.model.vo.CurationExposureAttribute;

public record CurationExposureAttributeRequest(CurationLayout layout) {

	public CurationExposureAttribute toVo() {
		return new CurationExposureAttribute(layout);
	}
}
