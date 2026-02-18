package spring.webmvc.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderPrice;

	private Long quantity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	public static OrderProduct create(Order order, Product product, Long quantity) {
		OrderProduct orderProduct = new OrderProduct();

		orderProduct.orderPrice = product.getPrice();
		orderProduct.quantity = quantity;
		orderProduct.product = product;
		orderProduct.order = order;

		product.removeQuantity(quantity);

		return orderProduct;
	}

	public void cancel() {
		product.addQuantity(quantity);
	}
}
