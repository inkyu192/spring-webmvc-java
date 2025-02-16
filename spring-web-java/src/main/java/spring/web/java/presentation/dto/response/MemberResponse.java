package spring.web.java.presentation.dto.response;

import java.time.OffsetDateTime;

import spring.web.java.infrastructure.util.DateTimeUtil;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.enums.MemberRole;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String city,
	String street,
	String zipcode,
	MemberRole role,
	OffsetDateTime createdAt
) {
	public MemberResponse(Member member) {
		this(
			member.getId(),
			member.getAccount(),
			member.getName(),
			member.getAddress().getCity(),
			member.getAddress().getStreet(),
			member.getAddress().getZipcode(),
			member.getRole(),
			DateTimeUtil.toOffsetDateTime(member.getCreatedAt())
		);
	}
}
