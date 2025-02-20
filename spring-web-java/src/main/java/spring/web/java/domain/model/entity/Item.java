package spring.web.java.domain.model.entity;

import org.springframework.http.HttpStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.domain.model.enums.Category;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends Base {

	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;
	private String name;
	private String description;
	private int price;
	private int quantity;

	@Enumerated(EnumType.STRING)
	private Category category;

	public static Item create(String name, String description, int price, int quantity, Category category) {
		Item item = new Item();

		item.name = name;
		item.description = description;
		item.price = price;
		item.quantity = quantity;
		item.category = category;

		return item;
	}

	public void update(String name, String description, int price, int quantity, Category category) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}

	public void removeQuantity(int quantity) {
		int differenceQuantity = this.quantity - quantity;

		if (differenceQuantity < 0) {
			throw new BaseException(ErrorCode.INSUFFICIENT_QUANTITY, HttpStatus.CONFLICT);
		}

		this.quantity = differenceQuantity;
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}
}
