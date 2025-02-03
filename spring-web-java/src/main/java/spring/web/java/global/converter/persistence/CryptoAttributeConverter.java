package spring.web.java.global.converter.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.global.common.AESCryptoUtils;

@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String s) {
		return AESCryptoUtils.encrypt(s);
	}

	@Override
	public String convertToEntityAttribute(String s) {
		return AESCryptoUtils.decrypt(s);
	}
}
