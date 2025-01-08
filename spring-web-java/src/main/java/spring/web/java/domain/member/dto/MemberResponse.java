package spring.web.java.domain.member.dto;

import spring.web.java.domain.member.Member;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String city,
	String street,
	String zipcode,
	Member.Role role
) {
	public MemberResponse(Member member) {
		this(
			member.getId(),
			member.getAccount(),
			member.getName(),
			member.getAddress().getCity(),
			member.getAddress().getStreet(),
			member.getAddress().getZipcode(),
			member.getRole()
		);
	}
}
