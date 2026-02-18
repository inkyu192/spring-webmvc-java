package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductUpdateCommand(
	Long id,
	ProductStatus status,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductAttributePutCommand attribute,
	ProductExposureAttribute exposureAttribute
) {
}
