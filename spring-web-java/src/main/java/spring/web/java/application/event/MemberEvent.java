package spring.web.java.application.event;

public record MemberEvent(
	String account,
	String name
) {
}
