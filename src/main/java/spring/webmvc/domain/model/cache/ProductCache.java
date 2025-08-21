package spring.webmvc.domain.model.cache;

import java.time.Instant;

import spring.webmvc.domain.model.enums.Category;

public record ProductCache(
	Long id,
	Category category,
	String name,
	String description,
	long price,
	long quantity,
	Instant createdAt
) {
	public static ProductCache create(
		Long id,
		Category category,
		String name,
		String description,
		long price,
		long quantity,
		Instant createdAt
	) {
		return new ProductCache(id, category, name, description, price, quantity, createdAt);
	}
}
