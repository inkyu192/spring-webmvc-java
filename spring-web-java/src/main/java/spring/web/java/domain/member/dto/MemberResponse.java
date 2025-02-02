package spring.web.java.domain.member.dto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import spring.web.java.domain.member.Member;

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
			member.getCreatedAt().atOffset(ZoneOffset.of("+09:00"))
		);
	}
}
