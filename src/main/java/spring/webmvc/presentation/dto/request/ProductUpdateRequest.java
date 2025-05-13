package spring.webmvc.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	property = "category",
	visible = true
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = TicketUpdateRequest.class, name = "TICKET"),
	@JsonSubTypes.Type(value = FlightUpdateRequest.class, name = "FLIGHT"),
	@JsonSubTypes.Type(value = AccommodationUpdateRequest.class, name = "ACCOMMODATION"),
})
@Getter
@RequiredArgsConstructor
public class ProductUpdateRequest {
	@NotNull
	private final Category category;
	@NotBlank
	private final String name;
	@NotBlank
	private final String description;
	@Min(100)
	private final int price;
	@Max(9999)
	private final int quantity;
}