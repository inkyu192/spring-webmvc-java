package spring.web.java.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import spring.web.java.domain.member.Member;

public record MemberUpdateRequest(
	@NotNull
	String name,
	@NotNull
	Member.Role role,
	@NotNull
	String city,
	@NotNull
	String street,
	@NotNull
	String zipcode
) {
}
