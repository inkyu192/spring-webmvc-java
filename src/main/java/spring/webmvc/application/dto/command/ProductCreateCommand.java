package spring.webmvc.application.dto.command;

import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductCreateCommand(
	ProductCategory category,
	String name,
	String description,
	Long price,
	Long quantity,
	ProductAttributePutCommand attribute,
	ProductExposureAttribute exposureAttribute
) {
}
