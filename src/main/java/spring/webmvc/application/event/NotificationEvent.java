package spring.webmvc.application.event;

public record NotificationEvent(
	Long memberId,
	String title,
	String message,
	String url
) {
}
