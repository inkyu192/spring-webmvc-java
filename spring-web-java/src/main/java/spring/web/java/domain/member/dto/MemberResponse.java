package spring.web.java.domain.member.dto;

import java.time.OffsetDateTime;

import spring.web.java.domain.member.Member;
import spring.web.java.global.common.DateTimeUtils;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String city,
	String street,
	String zipcode,
	Member.Role role,
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
			DateTimeUtils.toOffsetDateTime(member.getCreatedAt())
		);
	}
}
