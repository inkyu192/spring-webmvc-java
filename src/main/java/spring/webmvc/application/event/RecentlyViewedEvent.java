package spring.webmvc.application.event;

public record RecentlyViewedEvent(
	Long userId,
	Long productId
) {
}
