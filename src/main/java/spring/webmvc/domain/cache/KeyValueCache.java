package spring.webmvc.domain.cache;

import java.time.Duration;

public interface KeyValueCache {
	String get(String key);

	void set(String key, String value);

	void set(String key, String value, Duration timeout);

	boolean setIfAbsent(String key, String value, Duration timeout);

	boolean delete(String key);

	Long increment(String key, long delta);

	Long decrement(String key, long delta);
}
