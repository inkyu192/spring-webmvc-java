package spring.web.java.global.converter.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.global.common.AESCryptoUtils;

@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	private final AESCryptoUtils aesCryptoUtils;

	public CryptoAttributeConverter() {
		this.aesCryptoUtils = new AESCryptoUtils("d9ANIqIyfTygI92m6jWFfAzUbEP73TNB");
	}

	@Override
	public String convertToDatabaseColumn(String s) {
		return aesCryptoUtils.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return aesCryptoUtils.decrypt(s);
	}
}
