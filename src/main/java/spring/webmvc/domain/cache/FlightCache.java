package spring.webmvc.domain.cache;

public interface FlightCache {
	String get(Long id);

	void set(Long id, String value);
}