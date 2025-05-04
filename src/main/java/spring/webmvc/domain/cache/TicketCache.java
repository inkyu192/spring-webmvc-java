package spring.webmvc.domain.cache;

public interface TicketCache {
	String get(Long productId);

	void set(Long productId, String value);
}
