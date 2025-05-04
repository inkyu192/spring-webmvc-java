package spring.webmvc.domain.cache;

public interface AccommodationCache {
	String get(Long id);

	void set(Long id, String value);
}