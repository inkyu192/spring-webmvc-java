package spring.web.java.domain.member.dto;

public record MemberLoginRequest(
	String account,
	String password
) {
}
