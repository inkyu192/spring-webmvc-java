package spring.webmvc.domain.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.presentation.exception.OrderCancelNotAllowedException;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTime {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	private Instant orderedAt;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public List<OrderProduct> getOrderProducts() {
		return Collections.unmodifiableList(orderProducts);
	}

	public static Order create(Member member) {
		Order order = new Order();

		order.orderedAt = Instant.now();
		order.status = OrderStatus.ORDER;
		order.member = member;

		return order;
	}

	public void addProduct(Product product, int quantity) {
		orderProducts.add(OrderProduct.create(this, product, quantity));
	}

	public void cancel() {
		if (status == OrderStatus.CONFIRM) {
			throw new OrderCancelNotAllowedException(id);
		}

		status = OrderStatus.CANCEL;
		orderProducts.forEach(OrderProduct::cancel);
	}
}
