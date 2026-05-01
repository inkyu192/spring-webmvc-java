package spring.webmvc.domain.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.exception.InvalidEntityStatusException;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Instant orderedAt;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<OrderProduct> orderProducts = new ArrayList<>();

	public List<OrderProduct> getOrderProducts() {
		return Collections.unmodifiableList(orderProducts);
	}

	public static Order create(User user) {
		Order order = new Order();

		order.orderedAt = Instant.now();
		order.status = OrderStatus.ORDER;
		order.user = user;

		return order;
	}

	public void addProduct(Product product, Long quantity) {
		OrderProduct orderProduct = OrderProduct.create(this, product, quantity);

		orderProducts.add(orderProduct);
	}

	public void cancel() {
		if (status == OrderStatus.CONFIRM) {
			throw new InvalidEntityStatusException(
				Order.class,
				Objects.requireNonNull(id),
				status.name(),
				OrderStatus.CANCEL.name()
			);
		}

		status = OrderStatus.CANCEL;

		orderProducts.forEach(OrderProduct::cancel);
	}

	public void updateStatus(OrderStatus status) {
		if (this.status == OrderStatus.CONFIRM) {
			throw new InvalidEntityStatusException(
				Order.class,
				Objects.requireNonNull(id),
				this.status.name(),
				status.name()
			);
		}

		this.status = status;

		if (status == OrderStatus.CANCEL) {
			orderProducts.forEach(OrderProduct::cancel);
		}
	}
}
