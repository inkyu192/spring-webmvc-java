package spring.webmvc.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

public record ProductUpdateRequest(
	ProductStatus status,
	String name,
	String description,

	@Min(100)
	Long price,

	@Max(9999)
	Long quantity,

	@Valid
	@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
	@JsonSubTypes({
		@JsonSubTypes.Type(value = TransportPutRequest.class),
		@JsonSubTypes.Type(value = AccommodationPutRequest.class)
	})
	ProductAttributePutRequest attribute,

	ProductExposureAttribute exposureAttribute
) {
	public ProductUpdateCommand toCommand(Long id) {
		return new ProductUpdateCommand(
			id,
			status,
			name,
			description,
			price,
			quantity,
			attribute.toCommand(),
			exposureAttribute
		);
	}
}
