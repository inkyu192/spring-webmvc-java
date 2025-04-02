package spring.webmvc.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.CryptoAttributeConverter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination extends BaseTime {

	@Id
	@GeneratedValue
	@Column(name = "address_id")
	private Long id;

	@Convert(converter = CryptoAttributeConverter.class)
	private String recipient;

	@Convert(converter = CryptoAttributeConverter.class)
	private String phone;
	private String zipcode;

	@Convert(converter = CryptoAttributeConverter.class)
	private String address;
	private boolean isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public static Destination create(
		Member member,
		String recipient,
		String phone,
		String zipcode,
		String address,
		boolean isDefault
	) {
		Destination destination = new Destination();

		destination.member = member;
		destination.recipient = recipient;
		destination.phone = phone;
		destination.zipcode = zipcode;
		destination.address = address;
		destination.isDefault = isDefault;

		return destination;
	}
}
