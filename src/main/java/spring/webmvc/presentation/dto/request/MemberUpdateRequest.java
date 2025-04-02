package spring.webmvc.presentation.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
	String password,
	String name,
	@Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
	String phone,
	LocalDate birthDate
) {
}
