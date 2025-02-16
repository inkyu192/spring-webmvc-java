package spring.web.java.presentation.dto.request;

public record MemberLoginRequest(
	String account,
	String password
) {
}
