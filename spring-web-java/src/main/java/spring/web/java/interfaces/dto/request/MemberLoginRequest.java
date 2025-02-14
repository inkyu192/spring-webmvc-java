package spring.web.java.interfaces.dto.request;

public record MemberLoginRequest(
	String account,
	String password
) {
}
