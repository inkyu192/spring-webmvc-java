package spring.web.java.presentation.dto.response;

import java.time.Instant;

import spring.web.java.domain.model.entity.Member;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String city,
	String street,
	String zipcode,
	Instant createdAt
) {
	public MemberResponse(Member member) {
		this(
			member.getId(),
			member.getAccount(),
			member.getName(),
			member.getAddress().getCity(),
			member.getAddress().getStreet(),
			member.getAddress().getZipcode(),
			member.getCreatedAt()
		);
	}
}
