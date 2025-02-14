package spring.web.java.interfaces.dto.response;

import spring.web.java.domain.model.entity.Item;

public record ItemResponse(
	Long id,
	String name,
	String description,
	int price,
	int quantity
) {
	public ItemResponse(Item item) {
		this(
			item.getId(),
			item.getName(),
			item.getDescription(),
			item.getPrice(),
			item.getQuantity()
		);
	}
}
