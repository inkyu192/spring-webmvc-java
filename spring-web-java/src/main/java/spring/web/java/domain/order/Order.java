package spring.web.java.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Base;
import spring.web.java.domain.member.Member;
import spring.web.java.global.common.ResponseMessage;
import spring.web.java.global.exception.CommonException;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Base {

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private Status status;

	public static Order create(Member member, Delivery delivery, List<OrderItem> orderItems) {
		Order order = new Order();

		order.setMember(member);
		order.setDelivery(delivery);
		orderItems.forEach(order::setOrderItem);
		order.status = Status.ORDER;
		order.orderDate = LocalDateTime.now();

		return order;
	}

	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}

	public void setOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}

	public void cancel() {
		if (delivery.getStatus() == Delivery.Status.COMP) {
			throw new CommonException(ResponseMessage.ORDER_CANCEL_NOT_ALLOWED);
		}

		status = Status.CANCEL;
		orderItems.forEach(OrderItem::cancel);
		delivery.cancel();
	}

	@RequiredArgsConstructor
	public enum Status {
		ORDER("주문"),
		CANCEL("취소");

		private final String description;

		@JsonCreator
		public static Status of(Object name) {
			return Arrays.stream(Status.values())
				.filter(orderStatus -> orderStatus.name().equals(name))
				.findFirst()
				.orElse(null);
		}
	}
}
