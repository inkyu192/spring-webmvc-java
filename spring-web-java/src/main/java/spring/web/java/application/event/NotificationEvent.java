package spring.web.java.application.event;

public record NotificationEvent(
	Long memberId,
	String title,
	String message
) {
}
