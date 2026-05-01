package spring.webmvc.presentation.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;
import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;

public record CurationCreateRequest(
	String title,
	CurationPlacement placement,
	CurationType type,
	CurationAttributeRequest attribute,
	CurationExposureAttributeRequest exposureAttribute,
	Boolean isExposed,
	Long sortOrder,
	@Size(min = 1)
	List<CurationProductCreateRequest> products
) {
	public CurationCreateCommand toCommand() {
		return new CurationCreateCommand(
			title,
			placement,
			type,
			attribute != null ? attribute.toVo() : null,
			exposureAttribute != null ? exposureAttribute.toVo() : null,
			isExposed,
			sortOrder,
			products.stream()
				.map(CurationProductCreateRequest::toCommand)
				.toList()
		);
	}
}
