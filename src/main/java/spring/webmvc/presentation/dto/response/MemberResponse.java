package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.time.LocalDate;

import spring.webmvc.domain.model.entity.Member;

public record MemberResponse(
	Long id,
	String account,
	String name,
	String phone,
	LocalDate birthDate,
	Instant createdAt
) {
	public MemberResponse(Member member) {
		this(
			member.getId(),
			member.getAccount(),
			member.getName(),
			member.getPhone(),
			member.getBirthDate(),
			member.getCreatedAt()
		);
	}
}
