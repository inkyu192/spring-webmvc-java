package spring.webmvc.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.CryptoAttributeConverter;
import spring.webmvc.domain.model.enums.DeliveryStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTime {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id")
	private Long id;

	@Convert(converter = CryptoAttributeConverter.class)
	private String recipient;

	@Convert(converter = CryptoAttributeConverter.class)
	private String phone;
	private String zipcode;

	@Convert(converter = CryptoAttributeConverter.class)
	private String address;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Order order;

	public static Delivery create(Order order, Destination destination) {
		Delivery delivery = new Delivery();

		delivery.order = order;
		delivery.status = DeliveryStatus.READY;
		delivery.recipient = destination.getRecipient();
		delivery.phone = destination.getPhone();
		delivery.address = destination.getAddress();
		delivery.zipcode = destination.getZipcode();

		return delivery;
	}
}
