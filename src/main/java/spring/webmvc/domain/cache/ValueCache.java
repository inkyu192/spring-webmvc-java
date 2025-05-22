package spring.webmvc.domain.cache;

import java.time.Duration;

public interface ValueCache {
	String get(String key);

	<T> T get(String key, Class<T> clazz);

	<T> void set(String key, T value);

	<T> void set(String key, T value, Duration timeout);

	boolean setIfAbsent(String key, String value, Duration timeout);

	boolean delete(String key);

	Long increment(String key, long delta);

	Long decrement(String key, long delta);
}
