package spring.web.java.presentation.dto.response;

import java.time.Instant;

import spring.web.java.domain.model.entity.Item;

public record ItemResponse(
	Long id,
	String name,
	String description,
	int price,
	int quantity,
	Instant createdAt
) {
	public ItemResponse(Item item) {
		this(
			item.getId(),
			item.getName(),
			item.getDescription(),
			item.getPrice(),
			item.getQuantity(),
			item.getCreatedAt()
		);
	}
}
