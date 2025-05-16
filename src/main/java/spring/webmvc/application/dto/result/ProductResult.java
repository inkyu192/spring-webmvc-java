package spring.webmvc.application.dto.result;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;

@Getter
@RequiredArgsConstructor
public class ProductResult {
	private final Long id;
	private final Category category;
	private final String name;
	private final String description;
	private final long price;
	private final long quantity;
	private final Instant createdAt;

	public ProductResult(Product product) {
		id = product.getId();
		category = product.getCategory();
		name = product.getName();
		description = product.getDescription();
		price = product.getPrice();
		quantity = product.getQuantity();
		createdAt = product.getCreatedAt();
	}
}
