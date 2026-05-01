package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.enums.CurationLayout;
import spring.webmvc.domain.model.vo.CurationExposureAttribute;

public record CurationExposureAttributeResult(CurationLayout layout) {

	public static CurationExposureAttributeResult of(CurationExposureAttribute exposureAttribute) {
		if (exposureAttribute == null) {
			return new CurationExposureAttributeResult(CurationLayout.CAROUSEL);
		}
		return new CurationExposureAttributeResult(exposureAttribute.layout());
	}
}
