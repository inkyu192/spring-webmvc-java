package spring.webmvc.domain.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberType {
	FLIGHT("항공권"),
	ACCOMMODATION("숙박"),
	TICKET("티켓");

	private final String description;
}
