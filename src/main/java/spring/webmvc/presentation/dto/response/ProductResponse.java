package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.domain.model.entity.Product;

public record ProductResponse(
	Long id,
	String name,
	String description,
	int price,
	int quantity,
	Instant createdAt
) {
	public ProductResponse(Product product) {
		this(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getQuantity(),
			product.getCreatedAt()
		);
	}
}