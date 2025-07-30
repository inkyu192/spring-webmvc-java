package spring.webmvc.domain.model.vo;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.CryptoAttributeConverter;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {
	private static final Pattern PHONE_PATTERN = Pattern.compile("^01[016789]-\\d{3,4}-\\d{4}$");

	@Convert(converter = CryptoAttributeConverter.class)
	@Column(name = "phone")
	private String value;

	public static Phone create(String value) {
		if (!PHONE_PATTERN.matcher(value).matches()) {
			throw new IllegalArgumentException("Invalid phone number: " + value);
		}

		Phone phone = new Phone();
		phone.value = value;
		return phone;
	}
}
