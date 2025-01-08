package spring.web.java.domain.order;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.Address;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id")
	private Long id;

	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	private Status status;

	public static Delivery create(Address address) {
		Delivery delivery = new Delivery();

		delivery.status = Status.READY;
		delivery.address = address;

		return delivery;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void cancel() {
		this.status = Status.CANCEL;
	}

	@RequiredArgsConstructor
	public enum Status {
		READY("준비"),
		CANCEL("취소"),
		COMP("완료");

		private final String description;

		@JsonCreator
		public static Status of(Object name) {
			return Arrays.stream(Status.values())
				.filter(status -> status.name().equals(name))
				.findFirst()
				.orElse(null);
		}
	}

}
