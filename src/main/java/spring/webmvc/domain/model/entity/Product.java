package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.presentation.exception.InsufficientQuantityException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseCreator {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	private int price;
	private int quantity;

	@Enumerated(EnumType.STRING)
	private Category category;

	public static Product create(String name, String description, int price, int quantity, Category category) {
		Product product = new Product();
		product.name = name;
		product.description = description;
		product.price = price;
		product.quantity = quantity;
		product.category = category;
		return product;
	}

	public void update(String name, String description, int price, int quantity) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

	public void removeQuantity(int quantity) {
		int differenceQuantity = this.quantity - quantity;

		if (differenceQuantity < 0) {
			throw new InsufficientQuantityException(name, quantity, this.quantity);
		}

		this.quantity = differenceQuantity;
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}
}
