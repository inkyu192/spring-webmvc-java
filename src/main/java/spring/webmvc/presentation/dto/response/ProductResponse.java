package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import lombok.Getter;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class ProductResponse {
	private final Long id;
	private final Category category;
	private final String name;
	private final String description;
	private final long price;
	private final long quantity;
	private final Instant createdAt;

	public ProductResponse(ProductResult productResult) {
		id = productResult.getId();
		category = productResult.getCategory();
		name = productResult.getName();
		description = productResult.getDescription();
		price = productResult.getPrice();
		quantity = productResult.getQuantity();
		createdAt = productResult.getCreatedAt();
	}

	public ProductResponse(Product product) {
		id = product.getId();
		category = product.getCategory();
		name = product.getName();
		description = product.getDescription();
		price = product.getPrice();
		quantity = product.getQuantity();
		createdAt = product.getCreatedAt();
	}
}
