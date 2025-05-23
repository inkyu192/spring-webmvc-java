package spring.webmvc.domain.cache;

import java.time.Duration;
import java.util.Set;

public interface ZSetCache {
	<T> void add(String key, T value, double score);

	<T> void add(String key, T value, double score, Duration duration);

	<T> Set<T> range(String key, long start, long end, Class<T> clazz);
}
