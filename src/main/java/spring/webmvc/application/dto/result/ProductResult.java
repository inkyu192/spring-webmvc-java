package spring.webmvc.application.dto.result;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import spring.webmvc.domain.model.cache.ProductCache;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;

@Getter
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

	public ProductResult(ProductCache product) {
		id = product.id();
		category = product.category();
		name = product.name();
		description = product.description();
		price = product.price();
		quantity = product.quantity();
		createdAt = product.createdAt();
	}

	public ProductResult(Long id, Category category, String name, String description, long price, long quantity, Instant createdAt) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}
}
