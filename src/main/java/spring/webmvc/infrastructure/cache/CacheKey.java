package spring.webmvc.infrastructure.cache;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CacheKey {
	PRODUCT("products:%d", Duration.ofHours(1)),
	PRODUCT_STOCK("products:%d:stock", null),
	REFRESH_TOKEN("members:%s:token:refresh", Duration.ofDays(7)),
	REQUEST_LOCK("request-lock:%d:%s:%s", Duration.ofSeconds(1));

	private final String key;
	@Getter
	private final Duration timeout;

	public String generate(Object... args) {
		return key.formatted(args);
	}
}
