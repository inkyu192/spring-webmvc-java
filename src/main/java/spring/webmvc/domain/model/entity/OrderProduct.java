package spring.webmvc.domain.model.entity;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Order order;

	private int orderPrice;
	private int count;

	public static OrderProduct create(Order order, Product product, int count) {
		OrderProduct orderProduct = new OrderProduct();

		orderProduct.order = order;
		orderProduct.product = product;
		orderProduct.orderPrice = product.getPrice();
		orderProduct.count = count;

		product.removeQuantity(count);

		return orderProduct;
	}

	public void cancel() {
		product.addQuantity(count);
	}
}
