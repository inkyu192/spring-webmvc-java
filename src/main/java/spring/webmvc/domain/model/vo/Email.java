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
public class Email {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

	@Convert(converter = CryptoAttributeConverter.class)
	@Column(name = "email")
	private String value;

	public static Email create(String value) {
		if (!EMAIL_PATTERN.matcher(value).matches()) {
			throw new IllegalArgumentException("Invalid email address: " + value);
		}

		Email email = new Email();
		email.value = value;
		return email;
	}
}
