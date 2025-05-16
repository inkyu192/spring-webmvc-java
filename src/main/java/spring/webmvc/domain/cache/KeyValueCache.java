package spring.webmvc.domain.cache;

import java.time.Duration;

public interface KeyValueCache {
	String get(String key);

	void set(String key, String value, Duration duration);

	boolean setIfAbsent(String key, String value, Duration duration);
}
