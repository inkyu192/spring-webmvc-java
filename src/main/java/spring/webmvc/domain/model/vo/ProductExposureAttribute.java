package spring.webmvc.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductExposureAttribute(
	Boolean isPromotional,
	Boolean isNewArrival,
	Boolean isFeatured,
	Boolean isLowStock
) {
}
