package spring.web.java.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.domain.model.enums.DeliveryStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends Base {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id")
	private Long id;

	private String city;
	private String street;
	private String zipcode;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	public static Delivery create(String city, String street, String zipcode) {
		Delivery delivery = new Delivery();

		delivery.status = DeliveryStatus.READY;
		delivery.city = city;
		delivery.street = street;
		delivery.zipcode = zipcode;

		return delivery;
	}
}
