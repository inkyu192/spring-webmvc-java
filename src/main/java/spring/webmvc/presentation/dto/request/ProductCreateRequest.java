package spring.webmvc.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductCreateRequest(
	ProductCategory category,
	String name,
	String description,

	@Min(100)
	Long price,

	@Max(9999)
	Long quantity,

	@Valid
	@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
		property = "category"
	)
	@JsonSubTypes({
		@JsonSubTypes.Type(name = "TRANSPORT", value = TransportPutRequest.class),
		@JsonSubTypes.Type(name = "ACCOMMODATION", value = AccommodationPutRequest.class)
	})
	ProductAttributePutRequest attribute,

	ProductExposureAttribute exposureAttribute
) {
	public ProductCreateCommand toCommand() {
		return new ProductCreateCommand(
			category,
			name,
			description,
			price,
			quantity,
			attribute.toCommand(),
			exposureAttribute
		);
	}
}
