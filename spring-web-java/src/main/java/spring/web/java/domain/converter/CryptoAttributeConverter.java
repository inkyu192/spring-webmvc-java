package spring.web.java.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.infrastructure.util.AESCryptoUtil;

@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	private final AESCryptoUtil aesCryptoUtil;

	public CryptoAttributeConverter() {
		this.aesCryptoUtil = new AESCryptoUtil("d9ANIqIyfTygI92m6jWFfAzUbEP73TNB");
	}

	@Override
	public String convertToDatabaseColumn(String s) {
		return aesCryptoUtil.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return aesCryptoUtil.decrypt(s);
	}
}
