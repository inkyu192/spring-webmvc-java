package spring.web.java.presentation.dto.response;

import java.time.OffsetDateTime;

import spring.web.java.infrastructure.util.DateTimeUtil;
import spring.web.java.domain.model.entity.Member;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String city,
	String street,
	String zipcode,
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
			DateTimeUtil.toOffsetDateTime(member.getCreatedAt())
		);
	}
}
