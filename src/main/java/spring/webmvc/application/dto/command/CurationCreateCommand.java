package spring.webmvc.application.dto.command;

import java.util.List;

import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.vo.CurationAttribute;
import spring.webmvc.domain.model.vo.CurationExposureAttribute;

public record CurationCreateCommand(
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationAttribute attribute,
	CurationExposureAttribute exposureAttribute,
	Boolean isExposed,
	Long sortOrder,
	List<CurationProductCreateCommand> products
) {
}
