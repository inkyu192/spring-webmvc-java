package spring.webmvc.domain.cache;

import java.util.Optional;

public interface TicketCache {
	Optional<String> get(Long id);
	void set(Long id, String value);
}
