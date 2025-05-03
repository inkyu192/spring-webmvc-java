package spring.webmvc.domain.cache;

import java.util.Optional;

public interface TokenCache {
	Optional<String> get(Long memberId);
	void set(Long memberId, String value);
}
