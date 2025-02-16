package spring.web.java.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import spring.web.java.domain.model.enums.MemberRole;

public record MemberUpdateRequest(
	@NotNull
	String name,
	@NotNull
	MemberRole role,
	@NotNull
	String city,
	@NotNull
	String street,
	@NotNull
	String zipcode
) {
}
