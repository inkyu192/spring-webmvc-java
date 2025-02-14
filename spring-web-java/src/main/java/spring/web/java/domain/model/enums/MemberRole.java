package spring.web.java.domain.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
	ROLE_ADMIN("어드민"),
	ROLE_BUYER("소비자"),
	ROLE_SELLER("판매자");

	private final String description;

	@JsonCreator
	public static MemberRole of(Object name) {
		return Arrays.stream(MemberRole.values())
			.filter(role -> role.name().equals(name))
			.findFirst()
			.orElse(null);
	}
}
